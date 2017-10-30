package com.jetbrains.plugin.idea.nonsource.comments.components

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ProjectComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.jetbrains.plugin.idea.nonsource.comments.model.Comment
import org.jdom.Element

/**
 * @author demiurg
 *         29.10.17
 */

@State(name = "CommentService")
@Storage("nonsource_comments.xml")
class CommentsState : ProjectComponent, PersistentStateComponent<Element> {
    private val COMMENTS_ELEMENT_NAME = "comments"
    private val FILE_ELEMENT_NAME = "file"
    private val URL_ATTRIBUTE = "url"
    private val COMMENT_ELEMENT_NAME = "comment"
    private val TEXT_ATTRIBUTE = "text"
    private val LINE_ATTRIBUTE = "line"

    val comments: MutableMap<VirtualFile, MutableList<Comment>> = mutableMapOf()

    override fun loadState(state: Element?) {
        if (state == null) {
            return
        }
        val vfsManager = VirtualFileManager.getInstance()
        for (fileNode in state.children) {
            val url = fileNode.getAttributeValue(URL_ATTRIBUTE) ?: continue
            val file = vfsManager.findFileByUrl(url) ?: continue
            val comments = mutableListOf<Comment>()
            for (child in fileNode.children) {
                val text = child.getAttributeValue(TEXT_ATTRIBUTE) ?: continue
                val line = child.getAttribute(LINE_ATTRIBUTE).intValue
                comments.add(Comment.build(text, file, line))
            }
            this.comments[file] = comments
        }
    }

    override fun getState(): Element? {
        val state = Element(COMMENTS_ELEMENT_NAME)
        this.comments.forEach {file, comments ->
            val fileNode = Element(FILE_ELEMENT_NAME)
            fileNode.setAttribute(URL_ATTRIBUTE, file.url)
            comments.forEach {
                fileNode.addContent(Element(COMMENT_ELEMENT_NAME)
                        .setAttribute(TEXT_ATTRIBUTE, it.text)
                        .setAttribute(LINE_ATTRIBUTE, it.hook.line.toString())
                )
            }
            state.addContent(fileNode)
        }
        return state
    }
}