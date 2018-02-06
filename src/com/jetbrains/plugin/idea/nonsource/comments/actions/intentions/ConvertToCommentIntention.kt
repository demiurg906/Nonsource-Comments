package com.jetbrains.plugin.idea.nonsource.comments.actions.intentions

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

class ConvertToCommentIntention : AbstractIntention() {
    override fun getText(): String = "Convert line/selected to comment"

    override fun isAvailable(project: Project, editor: Editor, file: PsiFile): Boolean {
        return super.isAvailable(project, editor, file) && !isCommentedLine(project, editor, file)
    }

    override fun invoke(project: Project, editor: Editor, file: PsiFile) {
        val document = editor.document
        val caret = editor.caretModel.currentCaret
        val commentOffset: Int
        val startOffset: Int
        val endOffset: Int
        var text: String
        if (caret.hasSelection()) {
            startOffset = caret.selectionStart
            endOffset = caret.selectionEnd
            commentOffset = editor.startOffset(caret.selectionStartPosition.line)
            text = caret.selectedText ?: return
        } else {
            val line = caret.logicalPosition.line
            startOffset = editor.startOffset(line)
            endOffset = editor.startOffset(line + 1) - 1
            commentOffset = startOffset
            text = document.getText(TextRange(startOffset, endOffset))
        }
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