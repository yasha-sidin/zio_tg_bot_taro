package ru.otus
package dto.telegram

object MessageReactionCountUpdated {}

case class MessageReactionCountUpdated(
    chat: Chat,
    messageId: Long,
    date: Long,
    reactions: List[ReactionCount]
)
