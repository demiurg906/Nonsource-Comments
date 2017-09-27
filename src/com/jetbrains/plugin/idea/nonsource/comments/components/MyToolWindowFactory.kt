package com.jetbrains.plugin.idea.nonsource.comments.components

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.content.ContentManager
import com.jetbrains.plugin.idea.nonsource.comments.actions.MyAction
import java.awt.GridLayout
import javax.swing.JComponent
import javax.swing.JPanel

class MyToolWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val contentFactory = ContentFactory.SERVICE.getInstance()
        val contentManager = toolWindow.contentManager

        addTab(project, MyToolbarPanel(), "Hello", contentFactory, contentManager)
    }

    private fun addTab(project: Project, panel: MyToolbarPanel, titleKey: String, contentFactory: ContentFactory, contentManager: ContentManager) {
        val allFileContent = contentFactory.createContent(panel, titleKey, false)
        allFileContent.isCloseable = false
        contentManager.addContent(allFileContent)
    }
}

class MyToolbarPanel: SimpleToolWindowPanel(true, true) {
    init {
        setToolbar(toolbar())
    }

    val logger = Logger.getInstance(MyToolbarPanel::class.java)

    private fun toolbar(): JComponent {
        val toolBarPanel = JPanel(GridLayout())
        val actionGroup = DefaultActionGroup()
        try {
            val actionId = MyAction::class.java.name
            actionGroup.add(ActionManager.getInstance().getAction(actionId))
        } catch (e: Exception) {
            logger.error(e)
        }

        val actionToolbar = ActionManager.getInstance().createActionToolbar("codeCommentsViewToolbar", actionGroup, true)
        toolBarPanel.add(actionToolbar.component)
        return toolBarPanel
    }
}
