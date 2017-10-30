package com.jetbrains.plugin.idea.nonsource.comments.listeners

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.event.CaretEvent
import com.intellij.openapi.editor.event.CaretListener
import com.jetbrains.plugin.idea.nonsource.comments.services.CommentService
import com.jetbrains.plugin.idea.nonsource.comments.util.currentFile

/**
 * @author demiurg
 *         08.10.17
 */

/**
 * Listener курсора в editor
 * меняет состояние окна с комментами при перемещении курсора
 */
class EditorCaretListener : CaretListener {
    private val logger = Logger.getInstance(EditorCaretListener::class.java)

    override fun caretPositionChanged(e: CaretEvent) {
        if (e.oldPosition.line == e.newPosition.line) {
            // линия не поменялась, коммент обновлять не надо
            return
        }
        val project = e.editor.project ?: return
        val editor = e.editor
        val file = editor.currentFile() ?: return
        CommentService.getInstance(project).currentPosition = CommentService.Position(
                file,
                e.newPosition.line
        )
    }
}

