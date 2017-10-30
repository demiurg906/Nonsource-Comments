package com.jetbrains.plugin.idea.nonsource.comments.services

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.jetbrains.plugin.idea.nonsource.comments.components.CommentInitializer
import com.jetbrains.plugin.idea.nonsource.comments.components.CommentsState
import com.jetbrains.plugin.idea.nonsource.comments.components.MyToolbarEditor
import com.jetbrains.plugin.idea.nonsource.comments.model.Comment
import com.jetbrains.plugin.idea.nonsource.comments.services.CommentService.Position

/**
 * @author demiurg
 *         11.10.17
 */

open class CommentServiceImpl(val project: Project) : CommentService {
    private val logger = Logger.getInstance(CommentServiceImpl::class.java)

    private val comments: MutableMap<VirtualFile, MutableList<Comment>>
        get() {
            val state = project.getComponent(CommentsState::class.java)
            if (state == null) {
                logger.error("CommentsState not loaded")
                throw Exception()
            }
            return state.comments
        }

    private var toolbarEditor: MyToolbarEditor? = null

    override var currentComment: Comment? = null
        protected set(value) {
            field = value
        }

    override var currentPosition = Position()
        set(value) {
            // При обновлении позиции каретки обновляется коммент в тулбаре
            field = value
            currentComment = value.file?.let {
                val comments = getForFile(value.file)
                comments[value.line]
            }
            toolbarEditor?.updateDocumentText(currentComment)
        }

    override fun grabFocusToToolbar() {
        val comp = project.getComponent(CommentInitializer::class.java)
        val toolWindow = comp.toolWindow ?: return
        if (!toolWindow.isActive) {
            toolWindow.activate { toolbarEditor?.grabFocus() }
            return
        }
        toolbarEditor?.grabFocus()
    }

    override fun addNewComment() {
        // TODO: надо бы порефакторить эти две функции
        val file = currentPosition.file
        if (file == null) {
            logger.warn("current file is null")
            return
        }
        addNewComment(file, currentPosition.line)
    }

    override fun addNewComment(file: VirtualFile, line: Int) {
        val comment = Comment.build("", file, line)
        if (file !in comments.keys) {
            comments[file] = mutableListOf()
        }
        comments[file]?.add(comment)
        currentPosition = Position(file, line)
        EditorFactory.getInstance().refreshAllEditors()
    }

    override fun deleteEmptyComment(comment: Comment?) {
        if (comment == null) {
            return
        }
        if (comment.text == "") {
            val file = comment.hook.sourceFile
            val commentsList = comments[file]
            if (commentsList == null) {
                // TODO: мб лучше заменить на assert
                logger.warn("WTF? comment exists but list with comment isn't")
                return
            }
            commentsList.remove(comment)
            if (commentsList.isEmpty()) {
                comments.remove(file, commentsList)
            }
            currentComment = null
        }
        EditorFactory.getInstance().refreshAllEditors()
    }

    override fun getForFile(file: VirtualFile): Map<Int, Comment> {
        val comments = this.comments[file] ?: return mapOf()
        return comments.associate { it.hook.line to it }
    }

    override fun registerToolbarEditor(toolbarEditor: MyToolbarEditor) {
        this.toolbarEditor = toolbarEditor
    }
}