package com.jetbrains.plugin.idea.nonsource.comments.components

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.EditorKind
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.content.ContentManager
import com.intellij.util.ui.components.BorderLayoutPanel
import com.jetbrains.plugin.idea.nonsource.comments.actions.MyAction
import javax.swing.JComponent

class MyToolWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val contentFactory = ContentFactory.SERVICE.getInstance()
        val contentManager = toolWindow.contentManager

        addTab(project, MyToolbarPanel(project), "Hello", contentFactory, contentManager)
    }

    private fun addTab(project: Project, panel: MyToolbarPanel, titleKey: String, contentFactory: ContentFactory, contentManager: ContentManager) {
        val allFileContent = contentFactory.createContent(panel, titleKey, false)
        allFileContent.isCloseable = false
        contentManager.addContent(allFileContent)
    }
}

class MyToolbarPanel(project: Project): SimpleToolWindowPanel(true, true) {
    init {
        setToolbar(toolbar(project))
    }

    val logger = Logger.getInstance(MyToolbarPanel::class.java)

    private fun toolbar(project: Project): JComponent {
        val toolBarPanel = BorderLayoutPanel()
        val actionGroup = DefaultActionGroup()
        actionGroup.add(ActionManager.getInstance().getAction(MyAction.ID))

        val actionToolbar = ActionManager.getInstance().createActionToolbar("codeCommentsViewToolbar", actionGroup, true)
        toolBarPanel.addToTop(actionToolbar.component)

        val editorFactory = EditorFactory.getInstance()
        val document = editorFactory.createDocument("")
        val editor = editorFactory.createEditor(document, project, EditorKind.MAIN_EDITOR)
        toolBarPanel.addToCenter(editor.component)
        return toolBarPanel
    }
}
