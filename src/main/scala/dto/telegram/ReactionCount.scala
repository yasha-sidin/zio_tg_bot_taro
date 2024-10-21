package ru.otus
package dto.telegram

object ReactionCount {}

case class ReactionCount(
    `type`: ReactionType,
    totalCount: Long
)
