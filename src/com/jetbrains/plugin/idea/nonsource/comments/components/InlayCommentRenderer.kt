package com.jetbrains.plugin.idea.nonsource.comments.components

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorCustomElementRenderer
import com.intellij.openapi.editor.impl.ComplementaryFontsRegistry
import com.intellij.openapi.editor.impl.EditorImpl
import com.intellij.openapi.editor.impl.FontInfo
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.util.ui.GraphicsUtil
import com.intellij.xdebugger.ui.DebuggerColors
import com.jetbrains.plugin.idea.nonsource.comments.model.Comment
import java.awt.Font
import java.awt.Graphics
import java.awt.Rectangle
import javax.swing.UIManager.getFont

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

    override fun paint(editor: Editor, g: Graphics, targetRegion: Rectangle, textAttributes: TextAttributes) {
        if (comment.text == "") {
            return
        }

        val lines = comment.text.split("\n")
        var text = lines[0]
        if (lines.size > 1) {
            text += "..."
        }

        val attributes = editor.colorsScheme.getAttributes(DefaultLanguageHighlighterColors.INLINE_PARAMETER_HINT) ?: return

        val fontInfo = getFontInfo(editor)
        val metrics = fontInfo.fontMetrics()
        val font = fontInfo.font
        val lineHeight = Math.ceil(font.createGlyphVector(metrics.fontRenderContext, "Ap").visualBounds.height).toInt()
        val BACKGROUND_ALPHA = 0.55f

        val backgroundColor = attributes.backgroundColor
        if (backgroundColor != null) {
            val config = GraphicsUtil.setupAAPainting(g)
            GraphicsUtil.paintWithAlpha(g, BACKGROUND_ALPHA)
            g.color = backgroundColor
            val gap = if (targetRegion.height < lineHeight + 2) 1 else 2
            g.fillRoundRect(targetRegion.x + 2, targetRegion.y + gap, targetRegion.width - 4, targetRegion.height - gap * 2, 8, 8)
            config.restore()
        }
        val foregroundColor = attributes.foregroundColor
        if (foregroundColor != null) {
            g.color = foregroundColor
            g.font = getFont(editor)
            val savedClip = g.clip
            g.clipRect(targetRegion.x + 3, targetRegion.y + 2, targetRegion.width - 6, targetRegion.height - 4)
            val editorAscent = (editor as? EditorImpl)?.ascent ?: 0
            g.drawString(text, targetRegion.x + 7, targetRegion.y + Math.max(editorAscent, (targetRegion.height + metrics.ascent - metrics.descent) / 2) - 1)
            g.clip = savedClip
        }

//        val attributes = editor.colorsScheme.getAttributes(DebuggerColors.INLINED_VALUES_EXECUTION_LINE) ?: return
//        val fgColor = attributes.foregroundColor ?: return
//        g.color = fgColor
//        val fontInfo = getFontInfo(editor)
//        g.font = fontInfo.font
//        val metrics = fontInfo.fontMetrics()
//        g.drawString(comment.text, targetRegion.x, targetRegion.y + metrics.ascent)
    }

    override fun calcWidthInPixels(editor: Editor): Int {
        val fontInfo = getFontInfo(editor)
        return maxOf(1, fontInfo.fontMetrics().stringWidth(comment.text) + 14)
    }
}