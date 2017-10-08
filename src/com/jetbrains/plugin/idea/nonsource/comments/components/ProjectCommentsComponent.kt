package com.jetbrains.plugin.idea.nonsource.comments.components

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ProjectComponent
import com.jetbrains.plugin.idea.nonsource.comments.model.Comment

/**
 * @author demiurg
 *         08.10.17
 */

/**
 * Компонент, загружаемый при старте проекта
 * Хранит в себе информацию о всех комментах для всех файлов текущего проекта,
 * является персистентным
 *
 * При загрузке читает все сохраненные комменты и загружает их в память
 */
interface ProjectCommentsComponent: ProjectComponent, PersistentStateComponent<ProjectCommentsComponent.State> {
    class State {}

    /**
     * Возвращает все комменты текущего проекта
     */
    fun getAllComments(): Collection<Comment>

    /**
     * Добавляет новый коммент
     */
    fun addComment(comment: Comment)
}