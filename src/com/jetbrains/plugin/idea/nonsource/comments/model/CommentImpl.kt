package com.jetbrains.plugin.idea.nonsource.comments.model

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.Inlay
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.vfs.VirtualFile
import java.io.FileNotFoundException

/**
 * @author demiurg
 *         11.10.17
 */

class CommentImpl(text: String = "",
                  virtualFile: VirtualFile,
                  startOffset: Int) : Comment {
    // TODO: обсудить, хорошо ли это
    // на самом деле плохо (не проинициализируется, если файла нет)
    // добавить везде обработку на NPE?
    override lateinit var hook: CodeHook

    override var text: String
        get() = document.text
        set(value) {
            val appManager = ApplicationManager.getApplication()
            val action = { appManager.runWriteAction { document.setText(value) } }
            if (appManager.isDispatchThread) {
                action()
            } else {
                appManager.invokeLater(action)
            }
        }
    override var inlay: Inlay? = null
    override val document: Document = EditorFactory.getInstance().createDocument(text)

    init {
        // TODO: мб стоит бросать другое иссключение
        ApplicationManager.getApplication().runReadAction {
            val realDocument = FileDocumentManager.getInstance().getDocument(virtualFile) ?:
                    throw FileNotFoundException("file $virtualFile is missing")
            this.hook = CodeHookImpl(virtualFile, realDocument.createRangeMarker(startOffset, startOffset + 1, true))
        }
    }

    override fun toString(): String = "CommentImpl(${hook.sourceFile.name}:${hook.rangeMarker.startOffset}:'$text')"
}