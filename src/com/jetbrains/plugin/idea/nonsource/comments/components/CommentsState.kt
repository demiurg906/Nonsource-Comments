package com.jetbrains.plugin.idea.nonsource.comments.components

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ProjectComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.MessageType
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.wm.WindowManager
import com.intellij.ui.awt.RelativePoint
import com.jetbrains.plugin.idea.nonsource.comments.model.Comment
import org.jdom.Element
import javax.swing.event.HyperlinkEvent


/**
 * @author demiurg
 *         29.10.17
 */

@State(name = "CommentService")
@Storage("nonsource_comments.xml")
class CommentsState(private val project: Project) : ProjectComponent, PersistentStateComponent<Element> {
    private companion object {
        val COMMENTS_ELEMENT_NAME = "comments"
        val FILE_ELEMENT_NAME = "file"
        val URL_ATTRIBUTE = "url"
        val COMMENT_ELEMENT_NAME = "comment"
        val TEXT_ATTRIBUTE = "text"
        val START_OFFSET_ATTRIBUTE = "start_offset"
        val LINE_HASH = "line_hash"
    }

    data class Conflict(
            val commentText: String,
            val file: VirtualFile,
            val lineNumber: Int,
            val oldLine: String,
            val newLine: String
    )

    private val logger = Logger.getInstance(CommentsState::class.java)

    val comments: MutableMap<VirtualFile, MutableList<Comment>> = mutableMapOf()

    override fun loadState(state: Element?) {
        if (state == null) {
            return
        }

        val conflicts = mutableListOf<Conflict>()
        val vfsManager = VirtualFileManager.getInstance()
        for (fileNode in state.children) {
            val url = fileNode.getAttributeValue(URL_ATTRIBUTE) ?: continue
            val file = vfsManager.findFileByUrl(url) ?: continue
            val currentLines = MyFileReader(file)
            val comments = mutableListOf<Comment>()
            for (child in fileNode.children) {
                val text = child.getAttributeValue(TEXT_ATTRIBUTE) ?: continue
                val offset = child.getAttribute(START_OFFSET_ATTRIBUTE).intValue
                val oldLine = child.getAttributeValue(LINE_HASH)
                // TODO: add exception handler
                val (currentLine, lineNumber) = currentLines.lineByOffset(offset)
                // TODO: change to normal handler
                if (oldLine != currentLine) {
                    conflicts.add(Conflict(text, file, lineNumber, oldLine, currentLine))
                }
                comments.add(Comment.build(text, file, offset))
            }
            this.comments[file] = comments
        }
        showConflictsBalloon(conflicts)
    }

    private fun showConflictsBalloon(conflicts: List<Conflict>) {
        if (conflicts.isEmpty()) {
            return
        }
        conflicts.forEach {
            println("Conflict:")
            println("${it.file}:${it.lineNumber}")
            println("old line: ${it.oldLine}")
            println("new line: ${it.newLine}")
        }

        fun showBalloon() {
            // грязный хак, но это работает
            if (!project.isInitialized) {
                ApplicationManager.getApplication().invokeLater { showBalloon() }
                return
            }
            val statusBar = WindowManager.getInstance().getStatusBar(project)
            val body = "Your file(s) with comments has been changed.\n" +
                    "<a href=\"see\">See conflicts</a>"
            val balloon = JBPopupFactory.getInstance()
                    .createHtmlTextBalloonBuilder(body, MessageType.WARNING, { e: HyperlinkEvent ->
                        if (e.description != "see") {
                            logger.error("Undefined html action: ${e.description}")
                            return@createHtmlTextBalloonBuilder
                        }
                        // TODO: implement diff window
                        println("opening diff window")
                    })
                    .setTitle("Conflicts in comments")
                    .setHideOnLinkClick(true)
                    .createBalloon()
            balloon.show(RelativePoint.getSouthEastOf(statusBar.component), Balloon.Position.atRight)
        }

        showBalloon()
    }

    override fun getState(): Element? {
        val state = Element(COMMENTS_ELEMENT_NAME)
        this.comments.forEach {file, comments ->
            val fileNode = Element(FILE_ELEMENT_NAME)
            fileNode.setAttribute(URL_ATTRIBUTE, file.url)
            val lines = MyFileReader(file)
            comments.forEach {
                val offset = it.hook.rangeMarker.startOffset
                fileNode.addContent(Element(COMMENT_ELEMENT_NAME)
                        .setAttribute(TEXT_ATTRIBUTE, it.text)
                        .setAttribute(START_OFFSET_ATTRIBUTE, offset.toString())
                        .setAttribute(LINE_HASH, lines.lineByOffset(offset).first)
                )
            }
            state.addContent(fileNode)
        }
        return state
    }

    private class MyFileReader(file: VirtualFile) {
        // line number -> offset of start line
        private val offsets: List<Int>
        // line number -> hash (line)
        private val lines: Map<Int, String>
        // length of file
        private val length: Int

        init {
            val offsets = mutableListOf<Int>()
            val lines = mutableMapOf<Int, String>()
            var offset = 0
            val lineSeparatorLength = file.detectedLineSeparator?.length ?: 1
            file.inputStream.bufferedReader().useLines { fileLines ->
                fileLines.forEachIndexed { i, line ->
                    offsets.add(offset)
                    offset += line.length + lineSeparatorLength
                    lines[i] = line
                }
            }
            this.offsets = offsets
            this.lines = lines
            this.length = offset
        }

        fun lineByOffset(offset: Int): Pair<String, Int> {
            if (offset > length) {
                throw IllegalArgumentException("length of file is $length, and offset is $offset")
            }
            val line = offsets.indexOfLast { it <= offset }
            return lines[line]!! to line
        }
    }

}