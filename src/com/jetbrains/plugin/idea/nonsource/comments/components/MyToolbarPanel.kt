package com.jetbrains.plugin.idea.nonsource.comments.components

import com.intellij.ide.highlighter.JavaFileType
import com.intellij.lang.java.JavaLanguage
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFileFactory
import com.intellij.util.ui.components.BorderLayoutPanel
import com.jetbrains.plugin.idea.nonsource.comments.actions.AddCommentAction
import com.jetbrains.plugin.idea.nonsource.comments.services.CommentService
import javax.swing.JComponent

/**
 * @author demiurg
 *         11.10.17
 */

class MyToolbarPanel(project: Project) : SimpleToolWindowPanel(true, false) {
    val logger = Logger.getInstance(MyToolbarPanel::class.java)

    init {
//        setToolbar(toolbar(project))
//        val editor = toolbarEditor(project)
        val (editor, component) = toolbar(project)
        CommentService.getInstance(project).toolbarEditor = editor
//        setToolbar(editor.component)
//        add(editor.component)
        add(component)
    }

    private fun toolbarEditor(project: Project): MyToolbarEditor {
        val psiFile  = PsiFileFactory.getInstance(project).createFileFromText(JavaLanguage.INSTANCE, "")
        val document  = PsiDocumentManager.getInstance(project).getDocument(psiFile)
        return MyToolbarEditor(document, project, JavaFileType.INSTANCE)
    }



    private fun toolbar(project: Project): Pair<MyToolbarEditor, JComponent> {
        val toolBarPanel = BorderLayoutPanel()

        // добавление кнопочек
        val actionGroup = DefaultActionGroup()
        actionGroup.add(ActionManager.getInstance().getAction(AddCommentAction.ID))
        val actionToolbar = ActionManager.getInstance().createActionToolbar("codeCommentsViewToolbar", actionGroup, true)
        toolBarPanel.addToTop(actionToolbar.component)

        // добавление Editor'а
        // TODO: поменять на MyToolbarEditor

        val psiFile = PsiFileFactory.getInstance(project).createFileFromText(
                JavaLanguage.INSTANCE, ""
        )
        val document = PsiDocumentManager.getInstance(project).getDocument(psiFile)

//        val editorFactory = EditorFactory.getInstance()
//        val editor = editorFactory.createEditor(document!!, project, EditorKind.MAIN_EDITOR)
//        editor.scrollingModel.scrollToCaret(ScrollType.MAKE_VISIBLE)

        val editor = MyToolbarEditor(document, project, JavaFileType.INSTANCE)
        editor.addSettingsProvider { with(it.settings) {
            isLineNumbersShown = true
            isAdditionalPageAtBottom = true
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
        toolBarPanel.addToCenter(editor.component)

        CommentService.getInstance(project).toolbarEditor = editor

        return editor to toolBarPanel
    }

}
