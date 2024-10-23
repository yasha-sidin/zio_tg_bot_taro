package ru.otus
package dto.telegram

import java.time.Instant

import zio.json._

@jsonMemberNames(SnakeCase)
@jsonDerive
case class ChatMemberUpdated(
    chat: Chat,
    from: User,
    date: Instant,
    oldChatMember: ChatMember,
    newChatMember: ChatMember,
    inviteLink: Option[ChatInviteLink]
)
