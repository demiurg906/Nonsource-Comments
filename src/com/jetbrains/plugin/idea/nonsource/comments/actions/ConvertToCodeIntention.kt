package com.jetbrains.plugin.idea.nonsource.comments.actions

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.jetbrains.plugin.idea.nonsource.comments.services.CommentService

/**
 * @author demiurg
 *         30.12.2017
 */
class ConvertToCodeIntention : AbstractIntentionAction() {
    override fun getText(): String {
        return "Convert comment to code"
    }

    override fun isAvailable(project: Project, editor: Editor, file: PsiFile): Boolean {
        val superRes = super.isAvailable(project, editor, file)
        if (!superRes) {
            return superRes
        }
        val comment = CommentService.getInstance(project).currentComment
        return comment != null
    }

    override fun invoke(project: Project, editor: Editor, file: PsiFile) {
        val caret = editor.caretModel.currentCaret
        val commentService = CommentService.getInstance(project)
        val comment = commentService.currentComment ?: return
        ApplicationManager.getApplication().runWriteAction {
            editor.document.insertString(caret.offset, comment.text)
        }
        commentService.deleteComment(comment)
    }
}