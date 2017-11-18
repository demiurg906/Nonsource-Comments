package com.jetbrains.plugin.idea.nonsource.comments.services

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.jetbrains.plugin.idea.nonsource.comments.components.CommentInitializer
import com.jetbrains.plugin.idea.nonsource.comments.components.CommentsState
import com.jetbrains.plugin.idea.nonsource.comments.components.InlayCommentRenderer
import com.jetbrains.plugin.idea.nonsource.comments.components.MyToolbarEditor
import com.jetbrains.plugin.idea.nonsource.comments.model.Comment
import com.jetbrains.plugin.idea.nonsource.comments.services.CommentService.Position
import com.jetbrains.plugin.idea.nonsource.comments.util.currentFile

/**
 * @author demiurg
 *         11.10.17
 */

open class CommentServiceImpl(private val project: Project) : CommentService {
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
                comments[value.offset]
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
        addNewComment(file, currentPosition.offset)
    }

    override fun addNewComment(file: VirtualFile, offset: Int) {
        val comment = Comment.build("", file, offset)
        if (file !in comments.keys) {
            comments[file] = mutableListOf()
        }
        comments[file]?.add(comment)
        currentPosition = Position(file, offset)
        setInlay(comment)
        EditorFactory.getInstance().refreshAllEditors()

    }

    override fun setInlay(comment: Comment) {
        val offset = comment.hook.rangeMarker.startOffset
        val file = comment.hook.sourceFile
        EditorFactory.getInstance().allEditors
                .filter { it.currentFile() == file }
                .forEach { it.inlayModel.addInlineElement(offset, InlayCommentRenderer(comment)) }
    }

    override fun setAllInlays(editor: Editor) {
        val file = editor.currentFile() ?: return
        comments[file]?.forEach { editor.inlayModel.addInlineElement(it.hook.rangeMarker.startOffset, InlayCommentRenderer(it)) }
    }

    override fun deleteEmptyComment(comment: Comment?) {
        if (comment == null) {
            return
        }
        if (comment.text == "") {
            val file = comment.hook.sourceFile
            val commentsList = comments[file]
            if (commentsList == null) {
                logger.error("WTF? comment exists but list with comment isn't")
                throw IllegalStateException()
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
        return comments.associate { it.hook.rangeMarker.startOffset to it }
    }

    override fun registerToolbarEditor(toolbarEditor: MyToolbarEditor) {
        this.toolbarEditor = toolbarEditor
    }

    override fun deleteAllComments() {
        comments.clear()
    }
}