package com.jetbrains.plugin.idea.nonsource.comments.listeners

import com.intellij.openapi.editor.Editor
import com.jetbrains.plugin.idea.nonsource.comments.services.CommentService
import com.jetbrains.plugin.idea.nonsource.comments.util.currentFile
import java.awt.event.FocusEvent
import java.awt.event.FocusListener

/**
 * @author demiurg
 *         15.10.17
 */

class EditorFocusListener(val editor: Editor) : FocusListener {
    override fun focusGained(event: FocusEvent) {
        val project = editor.project ?: return
        val file = editor.currentFile() ?: return

        val commentService = CommentService.getInstance(project)
        commentService.currentPosition = CommentService.Position(file,
                editor.caretModel.currentCaret.logicalPosition.line
        )
    }

    override fun focusLost(event: FocusEvent?) {}
}