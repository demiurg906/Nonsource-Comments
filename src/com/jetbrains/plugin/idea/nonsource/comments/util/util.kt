package com.jetbrains.plugin.idea.nonsource.comments.util

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.LogicalPosition
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.vfs.VirtualFile

/**
 * @author demiurg
 *         15.10.17
 */

fun Editor.currentFile(): VirtualFile? = FileDocumentManager.getInstance().getFile(document)

fun Editor.currentLineOffset(): Int = startOffset(caretModel.currentCaret.logicalPosition)

fun Editor.startOffset(line: Int): Int = logicalPositionToOffset(LogicalPosition(line, 1))

fun Editor.startOffset(pos: LogicalPosition): Int = startOffset(pos.line)
