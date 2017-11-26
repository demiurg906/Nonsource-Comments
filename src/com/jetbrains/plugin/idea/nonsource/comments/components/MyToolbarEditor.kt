package com.jetbrains.plugin.idea.nonsource.comments.components

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.EditorFactory
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
        addFocusListener(object : FocusListener {
            val commentService = CommentService.getInstance(project)

            override fun focusGained(event: FocusEvent) {
                // при получении фокуса создаем новый пустой коммент
                if (commentService.currentComment == null) {
                    commentService.addNewComment()
                }
                if (currentComment == commentService.currentComment) {
                    return
                }
                currentComment = commentService.currentComment
                if (currentComment != null) {
                    setDocument(currentComment!!.document)
                }
            }

            override fun focusLost(event: FocusEvent) {
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
        val appManager = ApplicationManager.getApplication()
        appManager.runWriteAction {
            // TODO: добавить пустые строчки
            try {
                document = comment?.document ?: emptyDocument()
            } catch (e: IllegalStateException) {
                logger.error(e)
            }
        }
    }

    private fun emptyDocument(): Document = EditorFactory.getInstance().createDocument("")
}