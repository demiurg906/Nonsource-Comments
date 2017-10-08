package com.jetbrains.plugin.idea.nonsource.comments.model

import com.intellij.openapi.vfs.VirtualFile

/**
 * @author demiurg
 *         07.10.17
 */

/**
 * Comment data
 */
interface Comment {
    /**
     * Get text of comment
     */
    fun getComment(): String

    /**
     * Get hook to the commented line
     */
    fun getHook(): CodeHook
}

interface CodeHook {
    /**
     * Get file with source code
     */
    fun getSourceFile(): VirtualFile

    /**
     * Get commented line number
     */
    fun getLine(): Int
}

