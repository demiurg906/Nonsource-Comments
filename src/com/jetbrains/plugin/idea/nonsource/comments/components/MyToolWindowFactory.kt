package com.jetbrains.plugin.idea.nonsource.comments.components

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.content.ContentManager

/**
 * @author demiurg
 *         07.10.17
 */

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