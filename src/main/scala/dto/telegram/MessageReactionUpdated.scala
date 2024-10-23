package ru.otus
package dto.telegram

import dto.telegram.`type`.ReactionType
import zio.json._

@jsonMemberNames(SnakeCase)
@jsonDerive
case class MessageReactionUpdated(
    chat: Chat,
    messageId: Long,
    user: User,
    actorChat: Chat,
    date: Long,
    oldReaction: ReactionType,
    newReactionType: ReactionType
)
