package com.jetbrains.plugin.idea.nonsource.comments.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.jetbrains.plugin.idea.nonsource.comments.services.CommentService

/**
 * @author demiurg
 *         14.11.17
 */
class DeleteAllCommentsAction : AnAction() {
    companion object {
        const val ID = "Comments.DeleteAllCommentsAction"
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        CommentService.getInstance(project).deleteAllComments()
    }
}