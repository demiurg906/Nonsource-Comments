package com.jetbrains.plugin.idea.nonsource.comments.actions

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile
import com.jetbrains.plugin.idea.nonsource.comments.services.CommentService
import com.jetbrains.plugin.idea.nonsource.comments.util.startOffset

/**
 * @author demiurg
 *         30.12.17
 */

class ConvertToCommentIntention : AbstractIntentionAction() {
    override fun getText(): String {
        return "Convert line/selected to comment"
    }

    override fun invoke(project: Project, editor: Editor, file: PsiFile) {
        val document = editor.document
        val caret = editor.caretModel.currentCaret
        val commentOffset: Int
        var startOffset: Int
        var endOffset: Int
        var text: String
        if (caret.hasSelection()) {
            text = caret.selectedText ?: return
            startOffset = caret.selectionStart
            endOffset = caret.selectionEnd
            commentOffset = editor.startOffset(caret.selectionStartPosition.line)
        } else {
            val line = caret.logicalPosition.line
            startOffset = editor.startOffset(line)
            endOffset = editor.startOffset(line + 1)
            text = document.getText(TextRange(startOffset, endOffset))
            commentOffset = startOffset
        }
        startOffset += text.length - text.trimStart().length
        endOffset -= text.length - text.trimEnd().length
        text = text.trim()

        if (text == "") {
            return
        }
        CommentService.getInstance(project).addNewComment(
                file.virtualFile,
                commentOffset,
                text
        )
        ApplicationManager.getApplication().runWriteAction {
            document.deleteString(startOffset, endOffset)
        }
    }
}