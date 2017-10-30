package com.jetbrains.plugin.idea.nonsource.comments.model

import com.intellij.openapi.vfs.VirtualFile

/**
 * @author demiurg
 *         11.10.17
 */

/**
 * Comment data
 */
interface Comment {
    companion object {
        fun build(text: String, file: VirtualFile, line: Int): Comment = CommentImpl(text, file, line)
    }
    /**
     * text of comment
     */
    var text: String

    /**
     * hook to the commented line
     */
    val hook: CodeHook
}