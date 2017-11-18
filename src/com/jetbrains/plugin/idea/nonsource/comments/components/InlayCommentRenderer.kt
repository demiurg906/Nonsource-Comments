package com.jetbrains.plugin.idea.nonsource.comments.components

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorCustomElementRenderer
import com.intellij.openapi.editor.impl.ComplementaryFontsRegistry
import com.intellij.openapi.editor.impl.FontInfo
import com.intellij.xdebugger.ui.DebuggerColors
import com.jetbrains.plugin.idea.nonsource.comments.model.Comment
import java.awt.Font
import java.awt.Graphics
import java.awt.Rectangle

/**
 * @author demiurg
 *         12.11.17
 */
class InlayCommentRenderer(val comment: Comment) : EditorCustomElementRenderer {
    companion object {
        private fun getFontInfo(editor: Editor): FontInfo {
            val colorsScheme = editor.colorsScheme
            val fontPreferences = colorsScheme.fontPreferences
            val attributes = editor.colorsScheme.getAttributes(DebuggerColors.INLINED_VALUES_EXECUTION_LINE)
            val fontStyle = attributes?.fontType ?: Font.PLAIN
            return ComplementaryFontsRegistry.getFontAbleToDisplay('a'.toInt(), fontStyle, fontPreferences,
                    FontInfo.getFontRenderContext(editor.contentComponent))
        }
    }

    override fun paint(editor: Editor, g: Graphics, r: Rectangle) {
        if (comment.text == "") {
            return
        }
        val attributes = editor.colorsScheme.getAttributes(DebuggerColors.INLINED_VALUES_EXECUTION_LINE) ?: return
        val fgColor = attributes.foregroundColor ?: return
        g.color = fgColor
        val fontInfo = getFontInfo(editor)
        g.font = fontInfo.getFont()
        val metrics = fontInfo.fontMetrics()
        g.drawString(comment.text, r.x, r.y + metrics.getAscent())
    }

    override fun calcWidthInPixels(editor: Editor): Int {
        val fontInfo = getFontInfo(editor)
        return maxOf(1, fontInfo.fontMetrics().stringWidth(comment.text))
    }
}