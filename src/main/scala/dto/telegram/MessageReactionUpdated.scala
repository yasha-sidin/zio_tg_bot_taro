package ru.otus
package dto.telegram

object MessageReactionUpdated {}

case class MessageReactionUpdated(
    chat: Chat,
    messageId: Long,
    user: User,
    actorChat: Chat,
    date: Long,
    oldReaction: ReactionType,
    newReactionType: ReactionType
)
