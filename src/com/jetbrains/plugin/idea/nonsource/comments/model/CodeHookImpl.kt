package com.jetbrains.plugin.idea.nonsource.comments.model

import com.intellij.openapi.vfs.VirtualFile

/**
 * @author demiurg
 *         07.10.17
 */

class CodeHookImpl(override val sourceFile: VirtualFile,
                   override val line: Int) : CodeHook
