package com.jetbrains.plugin.idea.nonsource.comments.actions

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.openapi.application.Application
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile
import com.jetbrains.plugin.idea.nonsource.comments.services.CommentService

/**
 * @author demiurg
 *         30.12.17
 */

class ConvertToCommentAction : AbstractIntentionAction() {
    override fun getText(): String {
        return "Convert line/selected to comment"
    }

    override fun invoke(project: Project, editor: Editor, file: PsiFile) {
        val caret = editor.caretModel.currentCaret
        val document = editor.document
        if (caret.hasSelection()) {
            val text = caret.selectedText?.trim()
            if (text == null || text == "") {
                return
            }
            ApplicationManager.getApplication().runWriteAction {
                document.deleteString(caret.selectionStart, caret.selectionEnd)
            }
            CommentService.getInstance(project).addNewComment(
                    file.virtualFile,
                    caret.offset,
                    text
            )
        }
    }
}