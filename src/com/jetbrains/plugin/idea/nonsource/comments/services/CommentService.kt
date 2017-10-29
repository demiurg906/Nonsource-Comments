package com.jetbrains.plugin.idea.nonsource.comments.services

import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.jetbrains.plugin.idea.nonsource.comments.components.MyToolbarEditor
import com.jetbrains.plugin.idea.nonsource.comments.model.Comment

/**
 * @author demiurg
 *         08.10.17
 */

/**
 * Service for work with comments
 */
interface CommentService {
    companion object {
        fun getInstance(project: Project): CommentService = ServiceManager.getService(project, CommentService::class.java)
    }

    data class Position(val file: VirtualFile? = null, val line: Int = 0)

    /**
     * Creates new comment from current position
     */
    fun addNewComment()

    /**
     * Creates new comment from given position
     */
    fun addNewComment(file: VirtualFile, line: Int)

    /**
     * Delete current comment from map if it's empty
     */
    fun deleteEmptyComment(comment: Comment?)

    /**
     * Get all comments for file mapped to line number
     */
    fun getForFile(file: VirtualFile): Map<Int, Comment>

    /**
     * Active editor in toolbar
     */
    var toolbarEditor: MyToolbarEditor

    /**
     * Active chosen comment
     */
    // TODO: подумать, можно ли как-то изавиться от глобальных var переменных
    // можно от него избавиться, если хранить activeEditor
    val currentComment: Comment?

    var currentPosition: Position
}

