package com.jetbrains.plugin.idea.nonsource.comments.services

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.vfs.VirtualFileManager
import com.jetbrains.plugin.idea.nonsource.comments.model.Comment
import com.jetbrains.plugin.idea.nonsource.comments.model.CommentImpl
import org.jdom.Element

/**
 * @author demiurg
 *         11.10.17
 */

@State(name = "CommentService")
@Storage("nonsource_comments.xml")
class PersistentCommentServiceImpl : CommentServiceImpl(), PersistentStateComponent<Element>{
    private val COMMENTS_ELEMENT_NAME = "comments"
    private val FILE_ELEMENT_NAME = "file"
    private val URL_ATTRIBUTE = "url"
    private val COMMENT_ELEMENT_NAME = "comment"
    private val TEXT_ATTRIBUTE = "text"
    private val LINE_ATTRIBUTE = "line"

    override fun loadState(state: Element?) {
        if (state == null) {
            return
        }
        val vfsManager = VirtualFileManager.getInstance()
        state.children.forEach { fileNode ->
            val url = fileNode.getAttributeValue(URL_ATTRIBUTE) ?: return@forEach
            val file = vfsManager.findFileByUrl(url) ?: return@forEach
            val comments = mutableListOf<Comment>()
            fileNode.children.forEach {
                val text = it.getAttributeValue(TEXT_ATTRIBUTE) ?: return@forEach
                val line = it.getAttribute(LINE_ATTRIBUTE).intValue
                // TODO: переделать через builder
                comments.add(CommentImpl(text, file, line))
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