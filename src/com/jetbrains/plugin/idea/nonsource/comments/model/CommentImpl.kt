package com.jetbrains.plugin.idea.nonsource.comments.model

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Inlay
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
    // TODO: обсудить, хорошо ли это
    // на самом деле плохо (не проинициализируется, если файла нет)
    // добавить везде обработку на NPE?
    override lateinit var hook: CodeHook

    override var inlay: Inlay? = null

    init {
        // TODO: мб стоит бросать другое иссключение
        ApplicationManager.getApplication().runReadAction {
            val document = FileDocumentManager.getInstance().getDocument(virtualFile) ?:
                    throw FileNotFoundException("file $virtualFile is missing")
            this.hook = CodeHookImpl(virtualFile, document.createRangeMarker(startOffset, startOffset + 1, true))
        }
    }

    override fun toString(): String {
        return "CommentImpl(${hook.sourceFile.name}:${hook.rangeMarker.startOffset} '$text')"
    }
}