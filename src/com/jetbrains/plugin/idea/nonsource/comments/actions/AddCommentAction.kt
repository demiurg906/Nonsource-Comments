package com.jetbrains.plugin.idea.nonsource.comments.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.jetbrains.plugin.idea.nonsource.comments.actions.intentions.AddCommentIntention

/**
 * @author demiurg
 *         07.10.17
 */

@Deprecated("class will be removed soon")
class AddCommentAction : AnAction() {
    companion object {
        const val ID = "Comments.AddCommentAction"
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        AddCommentIntention().invoke(project, null, null)
    }
}