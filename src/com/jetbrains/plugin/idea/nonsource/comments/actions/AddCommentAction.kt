package com.jetbrains.plugin.idea.nonsource.comments.actions

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.jetbrains.plugin.idea.nonsource.comments.services.CommentService

/**
 * @author demiurg
 *         07.10.17
 */

class AddCommentAction : AnAction() {
    companion object {
        const val ID = "Comments.AddCommentAction"
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        AddCommentIntention().invoke(project, null, null)
    }
}