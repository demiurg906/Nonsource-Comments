package com.jetbrains.plugin.idea.nonsource.comments.actions.intentions

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.jetbrains.plugin.idea.nonsource.comments.services.CommentService

/**
 * @author demiurg
 *         30.12.2017
 */
class ConvertToCodeIntention : AbstractIntention() {
    override fun getText(): String = "Convert comment to code"

    override fun invoke(project: Project, editor: Editor, file: PsiFile) {
        val caret = editor.caretModel.currentCaret
        val commentService = CommentService.getInstance(project)
        val comment = commentService.currentComment ?: return
        ApplicationManager.getApplication().runWriteAction {
            editor.document.insertString(caret.offset, comment.text)
        }
        commentService.deleteComment(comment)
    }

    override fun isAvailable(project: Project, editor: Editor, file: PsiFile): Boolean {
        return super.isAvailable(project, editor, file) && isCommentedLine(project, editor, file)
    }
}