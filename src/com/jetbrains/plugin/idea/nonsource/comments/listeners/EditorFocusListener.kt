package com.jetbrains.plugin.idea.nonsource.comments.listeners

import com.intellij.openapi.editor.Editor
import com.jetbrains.plugin.idea.nonsource.comments.services.CommentService
import com.jetbrains.plugin.idea.nonsource.comments.util.currentFile
import com.jetbrains.plugin.idea.nonsource.comments.util.startOffset
import java.awt.event.FocusEvent
import java.awt.event.FocusListener

/**
 * @author demiurg
 *         15.10.17
 */

class EditorFocusListener(private val editor: Editor) : FocusListener {
    override fun focusGained(event: FocusEvent) {
        val project = editor.project ?: return
        val file = editor.currentFile() ?: return

        val commentService = CommentService.getInstance(project)
        val currentPosition = editor.caretModel.currentCaret.logicalPosition
        commentService.currentPosition = CommentService.Position(file,
                editor.startOffset(currentPosition)
        )
    }

    override fun focusLost(event: FocusEvent?) {}
}