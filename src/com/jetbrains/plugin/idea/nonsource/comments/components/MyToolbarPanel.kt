package com.jetbrains.plugin.idea.nonsource.comments.components

import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.fileTypes.PlainTextFileType
import com.intellij.openapi.fileTypes.PlainTextLanguage
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFileFactory
import com.intellij.util.ui.components.BorderLayoutPanel
import com.jetbrains.plugin.idea.nonsource.comments.services.CommentService
import javax.swing.JComponent

/**
 * @author demiurg
 *         11.10.17
 */

class MyToolbarPanel(project: Project) : SimpleToolWindowPanel(true, false) {
    init {
        val (editor, component) = toolbar(project)
        CommentService.getInstance(project).registerToolbarEditor(editor)
        add(component)
    }

    private fun toolbar(project: Project): Pair<MyToolbarEditor, JComponent> {
        // панель,содержащая в себе все, что лежит в тулбаре
        val toolBarPanel = BorderLayoutPanel()

        // добавление кнопочек
        // val actionGroup = DefaultActionGroup()
        // actionGroup.add(ActionManager.getInstance().getAction(AddCommentAction.ID))
        // actionGroup.add(ActionManager.getInstance().getAction(DeleteAllCommentsAction.ID))
        // val actionToolbar = ActionManager.getInstance().createActionToolbar("codeCommentsViewToolbar", actionGroup, true)
        // toolBarPanel.addToTop(actionToolbar.component)

        // добавление Editor'а
        val (language, fileType) = findFileType()
        val psiFile = PsiFileFactory.getInstance(project).createFileFromText(language, "")
        val document = PsiDocumentManager.getInstance(project).getDocument(psiFile)
        val editor = MyToolbarEditor(document, project, fileType)

        toolBarPanel.addToCenter(editor.component)

        return editor to toolBarPanel
    }


    private fun findFileType(): Pair<Language, FileType> {
        val language = Language.findLanguageByID("Markdown") ?: PlainTextLanguage.INSTANCE
        val fileType = language.associatedFileType ?: PlainTextFileType.INSTANCE
        return language to fileType
    }
}
