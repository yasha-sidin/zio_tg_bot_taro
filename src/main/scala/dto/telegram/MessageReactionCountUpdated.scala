package ru.otus
package dto.telegram

case class MessageReactionCountUpdated(
    chat: Chat,
    messageId: Long,
    date: Long,
    reactions: List[ReactionCount]
)
