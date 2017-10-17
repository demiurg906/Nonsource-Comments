package com.jetbrains.plugin.idea.nonsource.comments.model

import com.intellij.openapi.vfs.VirtualFile

/**
 * @author demiurg
 *         11.10.17
 */

class CommentImpl(override var text: String = "",
                  virtualFile: VirtualFile,
                  line: Int) : Comment {
    override val hook: CodeHook = CodeHookImpl(virtualFile, line)
}