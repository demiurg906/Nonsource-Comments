package com.jetbrains.plugin.idea.nonsource.comments.actions

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile

/**
 * @author demiurg
 *         30.12.2017
 */

abstract class AbstractIntention : IntentionAction {
    override fun getFamilyName(): String {
        return "Nonsource comments"
    }

    override fun startInWriteAction(): Boolean {
        return false
    }

    override fun isAvailable(project: Project, editor: Editor, file: PsiFile): Boolean {
        // TODO: мб стоит прочекать, что ничего не грузится
        return file.virtualFile.exists()
    }
}