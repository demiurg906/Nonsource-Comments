package com.jetbrains.plugin.idea.nonsource.comments.model

import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.vfs.VirtualFile
import java.io.FileNotFoundException

/**
 * @author demiurg
 *         11.10.17
 */

class CommentImpl(override var text: String = "",
                  virtualFile: VirtualFile,
                  startOffset: Int) : Comment {
    override val hook: CodeHook

    init {
        // TODO: мб стоит бросать другое иссключение
        val document = FileDocumentManager.getInstance().getDocument(virtualFile) ?:
                throw FileNotFoundException("file $virtualFile is missing")
        hook = CodeHookImpl(virtualFile, document.createRangeMarker(startOffset, startOffset + 1, true))
    }

    override fun toString(): String {
        return "CommentImpl(${hook.sourceFile.name}:${hook.rangeMarker.startOffset} '$text')"
    }
}