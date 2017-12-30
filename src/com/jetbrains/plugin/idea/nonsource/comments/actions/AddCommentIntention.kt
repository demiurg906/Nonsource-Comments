package com.jetbrains.plugin.idea.nonsource.comments.actions

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.jetbrains.plugin.idea.nonsource.comments.services.CommentService

/**
 * @author demiurg
 *         30.12.2017
 */

class AddCommentIntention : AbstractIntentionAction() {
    override fun getText(): String {
        return "Add comment to this line"
    }

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
        CommentService.getInstance(project).grabFocusToToolbar()
    }
}