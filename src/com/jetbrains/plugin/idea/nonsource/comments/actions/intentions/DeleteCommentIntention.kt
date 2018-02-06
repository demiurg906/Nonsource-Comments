package com.jetbrains.plugin.idea.nonsource.comments.actions.intentions

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.jetbrains.plugin.idea.nonsource.comments.services.CommentService

/**
 * @author demiurg
 *         04.02.2018
 */
class DeleteCommentIntention : AbstractIntention() {
    override fun getText(): String = "Delete comment"

    override fun isAvailable(project: Project, editor: Editor, file: PsiFile): Boolean {
        return super.isAvailable(project, editor, file) && isCommentedLine(project, editor, file)
    }

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
        val service = CommentService.getInstance(project)
        val comment = service.currentComment ?: return
        service.deleteComment(comment)
    }
}