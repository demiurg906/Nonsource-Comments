package com.jetbrains.plugin.idea.nonsource.comments.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.Logger

class MyAction: AnAction() {
    val logger = Logger.getInstance(MyAction::class.java)

    override fun actionPerformed(e: AnActionEvent?) {
        logger.info("Hello")
    }

}