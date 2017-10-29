package com.jetbrains.plugin.idea.nonsource.comments.model

import com.intellij.openapi.vfs.VirtualFile

/**
 * @author demiurg
 *         11.10.17
 */

class CommentImpl(override var text: String = "", override val hook: CodeHook) : Comment {
    constructor(text: String = "",
                virtualFile: VirtualFile,
                line: Int) : this(text, CodeHookImpl(virtualFile, line))

    override fun toString(): String {
        return "CommentImpl(${hook.sourceFile.name}:${hook.line} '$text')"
    }
}