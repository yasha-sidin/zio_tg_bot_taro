package ru.otus
package dto.telegram

import java.time.Instant

object ChatJoinRequest {}

case class ChatJoinRequest(
    chat: Chat,
    from: User,
    date: Instant,
    bio: Option[String],
    inviteLink: Option[ChatInviteLink]
)
