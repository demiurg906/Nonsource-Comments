package com.jetbrains.plugin.idea.nonsource.comments.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.Logger

/**
 * @author demiurg
 *         07.10.17
 */

/**
 * Вызывается с alt-enter для добавления коммента
 */
class AddCommentAction: AnAction() {
    override fun actionPerformed(e: AnActionEvent?) {
        TODO("not implemented")
    }
}

// TODO: удалить это бессмысленное действие
class MyAction : AnAction() {
    val logger = Logger.getInstance(MyAction::class.java)
    companion object {
        const val ID = "Comments.MyAction"
    }


    override fun actionPerformed(e: AnActionEvent) {
        logger.info("hello world")
        println("hello world")
    }
}
