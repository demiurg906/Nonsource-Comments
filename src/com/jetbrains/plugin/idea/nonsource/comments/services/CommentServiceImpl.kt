package com.jetbrains.plugin.idea.nonsource.comments.services

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.vfs.VirtualFile
import com.jetbrains.plugin.idea.nonsource.comments.components.MyToolbarEditor
import com.jetbrains.plugin.idea.nonsource.comments.model.Comment
import com.jetbrains.plugin.idea.nonsource.comments.model.CommentImpl
import com.jetbrains.plugin.idea.nonsource.comments.services.CommentService.Position

/**
 * @author demiurg
 *         11.10.17
 */

open class CommentServiceImpl : CommentService {
    private val logger = Logger.getInstance(CommentServiceImpl::class.java)

    protected val comments: MutableMap<VirtualFile, MutableList<Comment>> = mutableMapOf()

    // TODO: lateinit лучше заменить на null
    // или запихать куда-нибудь создание тулбара
    override lateinit var toolbarEditor: MyToolbarEditor
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
            try {
                toolbarEditor.updateDocumentText(currentComment)
            } catch (e: UninitializedPropertyAccessException) {
                // если тулбар еще не инициализирован, то ничего не делаем
            }
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
        // TODO: переделать генерацию коммента через builder
        val comment: Comment = CommentImpl("", file, line)
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
}