package com.jetbrains.plugin.idea.nonsource.comments.services

import com.intellij.openapi.vfs.VirtualFile
import com.jetbrains.plugin.idea.nonsource.comments.model.CodeHook
import com.jetbrains.plugin.idea.nonsource.comments.model.Comment

/**
 * @author demiurg
 *         08.10.17
 */

/**
 * Service for work with comments
 */
interface CommentService {
    /**
     * saves the comment
     */
    fun save(comment: Comment)

    /**
     * removes comments
     */
    fun remove(comments: Collection<Comment>)

    /**
     * Get all comments for file
     */
    fun getForFile(file: VirtualFile): Map<CodeHook, Comment>
}