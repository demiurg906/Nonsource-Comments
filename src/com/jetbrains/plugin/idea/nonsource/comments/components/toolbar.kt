package com.jetbrains.plugin.idea.nonsource.comments.components

import com.intellij.ide.highlighter.JavaFileType
import com.intellij.lang.java.JavaLanguage
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFileFactory
import com.intellij.ui.EditorTextField
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.content.ContentManager
import com.intellij.util.ui.components.BorderLayoutPanel
import com.jetbrains.plugin.idea.nonsource.comments.actions.MyAction
import javax.swing.JComponent

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

class MyToolbarPanel(project: Project): SimpleToolWindowPanel(true, false) {
    init {
        setToolbar(toolbar(project))
    }

    val logger = Logger.getInstance(MyToolbarPanel::class.java)

    private fun toolbar(project: Project): JComponent {
        val toolBarPanel = BorderLayoutPanel()

        // добавление кнопочек
        val actionGroup = DefaultActionGroup()
        actionGroup.add(ActionManager.getInstance().getAction(MyAction.ID))
        val actionToolbar = ActionManager.getInstance().createActionToolbar("codeCommentsViewToolbar", actionGroup, true)
        toolBarPanel.addToTop(actionToolbar.component)

        // добавление Editor'а
        // TODO: поменять на MyEditor

        val psiFile = PsiFileFactory.getInstance(project).createFileFromText(
                JavaLanguage.INSTANCE, ""
        )
        val document = PsiDocumentManager.getInstance(project).getDocument(psiFile)

//        val editorFactory = EditorFactory.getInstance()
//        val editor = editorFactory.createEditor(document!!, project, EditorKind.MAIN_EDITOR)
//        editor.scrollingModel.scrollToCaret(ScrollType.MAKE_VISIBLE)

        val editor = EditorTextField(document, project, JavaFileType.INSTANCE, false, false)
        editor.addSettingsProvider { with(it.settings) {
            isLineNumbersShown = true
//            isAdditionalPageAtBottom = true
            isFoldingOutlineShown = true
            isAnimatedScrolling = true
            isRefrainFromScrolling = true
            isRightMarginShown = true
            isLineMarkerAreaShown = true
            isSmartHome = true
            isVirtualSpace = true
            isCaretRowShown = true
            isDndEnabled = true
            isIndentGuidesShown = true
            isShowIntentionBulb = true
        } }

        editor.autoscrolls = true
        val e: Editor? = editor.editor
        toolBarPanel.addToCenter(editor.component)


        return toolBarPanel
    }
}
