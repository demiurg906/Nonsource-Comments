package com.jetbrains.plugin.idea.nonsource.comments.actions

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DataKeys
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.jetbrains.plugin.idea.nonsource.comments.services.CommentService
import com.jetbrains.plugin.idea.nonsource.comments.util.getFileFromEditor

/**
 * @author demiurg
 *         07.10.17
 */

/**
 * Вызывается с alt-enter для добавления коммента
 */
class AddCommentAction : AnAction(), IntentionAction {
    // TODO: отловить Intention Description Dir URL is null
    companion object {
        const val ID = "Comments.AddCommentAction"
    }

    val logger = Logger.getInstance(AddCommentAction::class.java)

    override fun getText(): String {
        return "Add comment to this line"
    }

    override fun getFamilyName(): String {
        return text
    }

    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean {
        // TODO: реализовать нормально
        return true
    }

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
        println("hello")
        val commentService = CommentService.getInstance(project)
        if (editor == null) {
            logger.warn("There is no active editor")
            return
        }
        val caret = editor.caretModel
        val line = caret.logicalPosition.line
        val virtualFile = getFileFromEditor(editor)
        if (virtualFile == null) {
            logger.warn("no file in AddCommentAction")
            return
        }

        commentService.addNewComment(virtualFile, line)
        commentService.toolbarEditor.grabFocus()
    }

    override fun startInWriteAction(): Boolean {
        // TODO: проверить, что именно true
        return true
    }

    override fun actionPerformed(e: AnActionEvent?) {
        // TODO: добавить вывод логов
        val project = e?.project ?: return
        val editor = e.getData(DataKeys.EDITOR)
        val file = e.getData(DataKeys.PSI_FILE)
        invoke(project, editor, file)
    }
}