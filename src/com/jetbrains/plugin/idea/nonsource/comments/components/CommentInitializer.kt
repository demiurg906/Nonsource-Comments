package com.jetbrains.plugin.idea.nonsource.comments.components

import com.intellij.openapi.Disposable
import com.intellij.openapi.components.ProjectComponent
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.event.EditorFactoryEvent
import com.intellij.openapi.editor.event.EditorFactoryListener
import com.jetbrains.plugin.idea.nonsource.comments.listeners.EditorCaretListener

/**
 * @author demiurg
 *         11.10.17
 */

class CommentInitializer : ProjectComponent, Disposable {
    override fun projectOpened() {
        EditorFactory.getInstance().addEditorFactoryListener(object : EditorFactoryListener {
            override fun editorCreated(event: EditorFactoryEvent) {
                val editor = event.editor
                editor.caretModel.addCaretListener(EditorCaretListener())
                // TODO: надо добавлять listener для всех кроме MyToolBarEditor
//                editor.contentComponent.addFocusListener(EditorFocusListener(editor))
            }

            override fun editorReleased(event: EditorFactoryEvent) {
                // TODO: вроде ничего не надо делать, но уточнить
            }
        }, this)
    }

    override fun dispose() {
        // TODO: понять, надо ли что-то делать
    }
}