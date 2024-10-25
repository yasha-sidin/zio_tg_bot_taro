package ru.otus
package dto.telegram

import zio.json._

@jsonMemberNames(SnakeCase)
@jsonDerive
case class ChatMemberUpdated(
    chat: Chat,
    from: User,
    date: Long,
    oldChatMember: ChatMember,
    newChatMember: ChatMember,
    inviteLink: Option[ChatInviteLink]
)
