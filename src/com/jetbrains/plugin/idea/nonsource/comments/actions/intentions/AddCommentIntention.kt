package com.jetbrains.plugin.idea.nonsource.comments.actions.intentions

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.jetbrains.plugin.idea.nonsource.comments.services.CommentService

/**
 * @author demiurg
 *         30.12.2017
 */

@Deprecated("class will be removed soon")
class AddCommentIntention : AbstractIntention() {
    override fun getText(): String = "Add comment to this line"

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
        CommentService.getInstance(project).grabFocusToToolbar()
    }

    override fun isAvailable(project: Project, editor: Editor, file: PsiFile): Boolean {
        return super.isAvailable(project, editor, file) && !isCommentedLine(project, editor, file)
    }
}