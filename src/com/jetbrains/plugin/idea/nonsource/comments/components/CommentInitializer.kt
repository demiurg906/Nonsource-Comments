package com.jetbrains.plugin.idea.nonsource.comments.components

import com.intellij.openapi.Disposable
import com.intellij.openapi.components.ProjectComponent
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.EditorGutterAction
import com.intellij.openapi.editor.LogicalPosition
import com.intellij.openapi.editor.event.EditorFactoryEvent
import com.intellij.openapi.editor.event.EditorFactoryListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowAnchor
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.ui.content.ContentFactory
import com.jetbrains.plugin.idea.nonsource.comments.listeners.EditorCaretListener
import com.jetbrains.plugin.idea.nonsource.comments.services.CommentService
import java.awt.Cursor

/**
 * @author demiurg
 *         11.10.17
 */

class CommentInitializer(private val project: Project) : ProjectComponent, Disposable {
    var toolWindow: ToolWindow? = null

    override fun initComponent() {
        project.getComponent(CommentsState::class.java)
    }

    override fun projectOpened() {
        val toolWindow = ToolWindowManager.getInstance(project)
                .registerToolWindow(
                        "Comments",
                        false,
                        ToolWindowAnchor.LEFT,
                        true
                )
        val contentFactory = ContentFactory.SERVICE.getInstance()
        val contentManager = toolWindow.contentManager
        val allFileContent = contentFactory.createContent(MyToolbarPanel(project), "Comments", false)
        allFileContent.isCloseable = false
        contentManager.addContent(allFileContent)

        this.toolWindow = toolWindow

        EditorFactory.getInstance().addEditorFactoryListener(object : EditorFactoryListener {
            override fun editorCreated(event: EditorFactoryEvent) {
                val editor = event.editor
                editor.caretModel.addCaretListener(EditorCaretListener())
                // TODO: надо добавлять listener для всех кроме MyToolBarEditor
                // editor.contentComponent.addFocusListener(EditorFocusListener(editor))

                val project = editor.project ?: return
                editor.gutter.registerTextAnnotation(CommentGutterAnnotation(CommentService.getInstance(project)),
                        object : EditorGutterAction {
                            val commentService = CommentService.getInstance(project)

                            override fun doAction(line: Int) {
                                editor.caretModel.currentCaret.moveToLogicalPosition(LogicalPosition(line, 0, true))
                                commentService.grabFocusToToolbar()
                            }

                            override fun getCursor(lineNum: Int): Cursor? {
                                return editor.component.cursor
                            }
                        })


            }

            override fun editorReleased(event: EditorFactoryEvent) {
                // TODO: вроде ничего не надо делать, но уточнить
            }
        }, this)
    }

    override fun dispose() {}
}