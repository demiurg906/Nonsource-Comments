package com.jetbrains.plugin.idea.nonsource.comments.util

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.vfs.VirtualFile

/**
 * @author demiurg
 *         15.10.17
 */

fun getFileFromEditor(editor: Editor): VirtualFile? = FileDocumentManager.getInstance().getFile(editor.document)