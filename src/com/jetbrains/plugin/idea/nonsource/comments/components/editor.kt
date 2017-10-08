package com.jetbrains.plugin.idea.nonsource.comments.components

import com.intellij.codeHighlighting.BackgroundEditorHighlighter
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorLocation
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.EditorTextField
import java.beans.PropertyChangeListener
import javax.swing.Icon
import javax.swing.JComponent

/**
 * @author demiurg
 *         08.10.17
 */

/**
 * Класс, представляющий editor в тулбаре комментариев
 * Обертка для EditorTextField
 * TODO: возможно вместо FileEditor лучше взять TextEditor
 */
class MyEditor: FileEditor {
    /**
     * instance EditorTextField, над которым происходят изменения
     */
    val editor: EditorTextField = EditorTextField()

    init {

    }

    override fun isModified(): Boolean {
        TODO("not implemented")
    }

    override fun addPropertyChangeListener(listener: PropertyChangeListener) {
        TODO("not implemented")
    }

    override fun getName(): String {
        TODO("not implemented")
    }

    override fun setState(state: FileEditorState) {
        TODO("not implemented")
    }

    override fun getComponent(): JComponent {
        TODO("not implemented")
    }

    override fun getPreferredFocusedComponent(): JComponent? {
        TODO("not implemented")
    }

    override fun <T : Any?> getUserData(key: Key<T>): T? {
        TODO("not implemented")
    }

    override fun selectNotify() {
        TODO("not implemented")
    }

    override fun <T : Any?> putUserData(key: Key<T>, value: T?) {
        TODO("not implemented")
    }

    override fun getCurrentLocation(): FileEditorLocation? {
        TODO("not implemented")
    }

    override fun deselectNotify() {
        TODO("not implemented")
    }

    override fun getBackgroundHighlighter(): BackgroundEditorHighlighter? {
        TODO("not implemented")
    }

    override fun isValid(): Boolean {
        TODO("not implemented")
    }

    override fun removePropertyChangeListener(listener: PropertyChangeListener) {
        TODO("not implemented")
    }

    override fun dispose() {
        TODO("not implemented")
    }
}

/**
 * Класс представляющий тип комментариев
 * TODO: возможно надо заменить на LanguageFileType
 * @see com.intellij.openapi.fileTypes.LanguageFileType
 */
class CommentFileType: FileType {
    override fun getDefaultExtension(): String {
        TODO("not implemented")
    }

    override fun getIcon(): Icon? {
        TODO("not implemented")
    }

    override fun getCharset(file: VirtualFile, content: ByteArray): String? {
        TODO("not implemented")
    }

    override fun getName(): String {
        TODO("not implemented")
    }

    override fun getDescription(): String {
        TODO("not implemented")
    }

    override fun isBinary(): Boolean {
        TODO("not implemented")
    }

    override fun isReadOnly(): Boolean {
        TODO("not implemented")
    }
}