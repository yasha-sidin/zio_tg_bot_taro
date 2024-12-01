package ru.otus
package dto.telegram

import dto.telegram.`type`.ReactionType

case class MessageReactionUpdated(
    chat: Chat,
    messageId: Long,
    user: User,
    actorChat: Chat,
    date: Long,
    oldReaction: ReactionType,
    newReactionType: ReactionType
)
