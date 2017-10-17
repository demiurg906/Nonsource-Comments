package com.jetbrains.plugin.idea.nonsource.comments.model

/**
 * @author demiurg
 *         11.10.17
 */

/**
 * Comment data
 */
interface Comment {
    /**
     * text of comment
     */
    var text: String

    /**
     * hook to the commented line
     */
    val hook: CodeHook
}