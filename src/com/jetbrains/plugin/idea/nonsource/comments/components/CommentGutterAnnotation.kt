package com.jetbrains.plugin.idea.nonsource.comments.components

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.TextAnnotationGutterProvider
import com.intellij.openapi.editor.colors.ColorKey
import com.intellij.openapi.editor.colors.EditorFontType
import com.jetbrains.plugin.idea.nonsource.comments.services.CommentService
import com.jetbrains.plugin.idea.nonsource.comments.util.currentFile
import com.jetbrains.plugin.idea.nonsource.comments.util.startOffset
import java.awt.Color

/**
 * @author demiurg
 *         18.10.17
 */
class CommentGutterAnnotation(private val commentService: CommentService) : TextAnnotationGutterProvider {

    private fun checkData(line: Int, editor: Editor?): Boolean {
        if (editor == null) {
            return false
        }
        val file = editor.currentFile() ?: return false
        val commentsMap = commentService.getForFile(file)
        val offset = editor.startOffset(line)
        val nextLineOffset = editor.startOffset(line + 1)
        return offset in commentsMap
    }

    override fun getPopupActions(line: Int, editor: Editor?): MutableList<AnAction> {
        return mutableListOf()
    }

    override fun getColor(line: Int, editor: Editor?): ColorKey? {
        if (!checkData(line, editor)) {
            return null
        }
        return ColorKey.createColorKey("CommentColor", Color.BLUE)
    }

    override fun getLineText(line: Int, editor: Editor?): String? {
        if (!checkData(line, editor)) {
            return null
        }
        return "C"
    }

    override fun getToolTip(line: Int, editor: Editor?): String? {
        if (!checkData(line, editor)) {
            return null
        }
        // TODO: вставить корректное
        return "tip"
    }

    override fun getStyle(line: Int, editor: Editor?): EditorFontType {
        return EditorFontType.BOLD
    }

    override fun getBgColor(line: Int, editor: Editor?): Color? {
        if (!checkData(line, editor)) {
            return null
        }
        return Color.YELLOW
    }

    override fun gutterClosed() {
        // TODO: подумать, надо ли
    }
}