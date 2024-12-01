package ru.otus
package dto.telegram

case class ChatJoinRequest(
    chat: Chat,
    from: User,
    date: Long,
    bio: Option[String],
    inviteLink: Option[ChatInviteLink]
)
