package com.jetbrains.plugin.idea.nonsource.comments.model

import com.intellij.openapi.vfs.VirtualFile

/**
 * @author demiurg
 *         11.10.17
 */

interface CodeHook {
    /**
     * file with source code
     */
    val sourceFile: VirtualFile

    /**
     * commented line number
     */
    val line: Int
}