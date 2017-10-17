package com.jetbrains.plugin.idea.nonsource.comments.services

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.vfs.VirtualFile
import com.jetbrains.plugin.idea.nonsource.comments.components.MyToolbarEditor
import com.jetbrains.plugin.idea.nonsource.comments.model.Comment
import com.jetbrains.plugin.idea.nonsource.comments.model.CommentImpl
import com.jetbrains.plugin.idea.nonsource.comments.services.CommentService.Position

/**
 * @author demiurg
 *         11.10.17
 */

//@State(name = "CommentService")
class CommentServiceImpl : CommentService {
    private val logger = Logger.getInstance(CommentServiceImpl::class.java)

    private var state: CommentService.State = CommentService.State(mutableMapOf())

    override lateinit var toolbarEditor: MyToolbarEditor
    override var currentComment: Comment? = null
        private set(value) {field = value}

    override var currentPosition = Position()
        set(value) {
            // При обновлении позиции каретки обновляется коммент в тулбаре
            field = value
            if (value.file == null) {
                currentComment = null;
            } else {
                val comments = getForFile(value.file)
                currentComment = comments[value.line]
            }
            try {
                toolbarEditor.updateDocumentText(currentComment)
            } catch (e: UninitializedPropertyAccessException) {
                // если тулбар еще не инициализирован, то ничего не делаем
            }
        }


    override fun addNewComment(file: VirtualFile, line: Int) {
        // TODO: переделать генерацию коммента через builder
        val comment: Comment = CommentImpl("", file, line)
        if (file !in state.comments.keys) {
            state.comments[file] = mutableListOf()
        }
        state.comments[file]?.add(comment)
        currentPosition = Position(file, line)
//        currentComment = text
    }

    override fun remove(comment: Comment) {
        state.comments[comment.hook.sourceFile]?.remove(comment)
    }

    override fun remove(comments: Collection<Comment>) {
        comments.forEach { remove(it) }
    }

    override fun getForFile(file: VirtualFile): Map<Int, Comment> {
        val comments = state.comments[file] ?: return mapOf()
        return comments.associate { it.hook.line to it }
    }


    // Persistence
//    override fun loadState(state: CommentService.State?) {
//        this.state = state!!
//    }
//
//    override fun getState(): CommentService.State? {
//        return state
//    }
}