package com.jetbrains.plugin.idea.nonsource.comments.components

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.Project
import com.intellij.ui.EditorTextField
import com.jetbrains.plugin.idea.nonsource.comments.model.Comment
import com.jetbrains.plugin.idea.nonsource.comments.services.CommentService
import java.awt.event.FocusEvent
import java.awt.event.FocusListener


/**
 * @author demiurg
 *         08.10.17
 */

/**
 * Класс, представляющий editor в тулбаре комментариев
 */
class MyToolbarEditor(
        document: Document?,
        project: Project,
        fileType: FileType
) : EditorTextField(document, project, fileType, false, false) {
    private val logger = Logger.getInstance(MyToolbarEditor::class.java)

    var currentComment: Comment? = null

    init {
        addDocumentListener(object: DocumentListener {
            override fun documentChanged(event: DocumentEvent?) {
                if (event == null) {
                    return
                }
                val commentService = CommentService.getInstance(project)
                val comment = currentComment ?: return
                val line = commentService.currentPosition.line
                val file = commentService.currentPosition.file
                if (file == null) {
                    logger.warn("current file is null")
                    return
                }
                if (line == comment.hook.line || file != comment.hook.sourceFile) {
                    comment.text = event.document.text
                }
            }
        })
        addFocusListener(object : FocusListener {
            val commentService = CommentService.getInstance(project)

            override fun focusGained(event: FocusEvent?) {
                // при получении фокуса создаем новый пустой коммент
                with(commentService) {
                    if (currentComment == null) {
                        addNewComment()
                    }
                }
                currentComment = commentService.currentComment
            }

            override fun focusLost(event: FocusEvent?) {
                // при потере фокуса трем коммент, если он пустой
                commentService.deleteEmptyComment(currentComment)
            }
        })
        addSettingsProvider { with(it.settings) {
            isLineNumbersShown = true
            isAdditionalPageAtBottom = true
            isLineMarkerAreaShown = true
            isCaretRowShown = true
            isIndentGuidesShown = true
        } }
    }

    fun updateDocumentText(comment: Comment?) {
        ApplicationManager.getApplication().runWriteAction {
            // TODO: добавить пустые строчки
            text = comment?.text ?: ""
        }
    }
}