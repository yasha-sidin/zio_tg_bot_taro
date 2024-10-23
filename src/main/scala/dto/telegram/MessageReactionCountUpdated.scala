package ru.otus
package dto.telegram

import zio.json._

@jsonMemberNames(SnakeCase)
@jsonDerive
case class MessageReactionCountUpdated(
    chat: Chat,
    messageId: Long,
    date: Long,
    reactions: List[ReactionCount]
)
