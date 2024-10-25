package ru.otus
package dto.telegram

import zio.json._

@jsonMemberNames(SnakeCase)
@jsonDerive
case class ChatJoinRequest(
    chat: Chat,
    from: User,
    date: Long,
    bio: Option[String],
    inviteLink: Option[ChatInviteLink]
)
