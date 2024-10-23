package ru.otus
package dto.telegram

import java.time.Instant

import zio.json._

@jsonMemberNames(SnakeCase)
@jsonDerive
case class ChatJoinRequest(
    chat: Chat,
    from: User,
    date: Instant,
    bio: Option[String],
    inviteLink: Option[ChatInviteLink]
)
