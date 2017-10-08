package com.jetbrains.plugin.idea.nonsource.comments.listeners

import com.intellij.openapi.editor.event.CaretEvent
import com.intellij.openapi.editor.event.CaretListener

/**
 * @author demiurg
 *         08.10.17
 */

/**
 * Listener курсора в editor
 * меняет состояние окна с комментами при перемещении курсора
 */
class MyCaretListener: CaretListener {
    override fun caretPositionChanged(e: CaretEvent?) {
        TODO("not implemented")
    }
}

