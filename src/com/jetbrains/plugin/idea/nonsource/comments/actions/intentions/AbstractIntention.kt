package com.jetbrains.plugin.idea.nonsource.comments.actions.intentions

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.jetbrains.plugin.idea.nonsource.comments.services.CommentService

/**
 * @author demiurg
 *         30.12.2017
 */

abstract class AbstractIntention : IntentionAction {
    override fun getFamilyName(): String = "Nonsource comments"

    override fun startInWriteAction(): Boolean {
        return false
    }

    override fun isAvailable(project: Project, editor: Editor, file: PsiFile): Boolean {
        return file.virtualFile.exists()
    }

    fun isCommentedLine(project: Project, editor: Editor, file: PsiFile): Boolean {
        val comment = CommentService.getInstance(project).currentComment
        return comment != null
    }
}