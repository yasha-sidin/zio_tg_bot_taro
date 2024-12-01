package ru.otus
package dto.telegram

case class ChatMemberUpdated(
    chat: Chat,
    from: User,
    date: Long,
    oldChatMember: ChatMember,
    newChatMember: ChatMember,
    inviteLink: Option[ChatInviteLink]
)
