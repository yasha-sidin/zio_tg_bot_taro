package ru.otus
package dto.telegram

import java.time.Instant

object ChatMemberUpdated {

}

case class ChatMemberUpdated(
                              chat: Chat,
                              from: User,
                              date: Instant,
                              oldChatMember: ChatMember,
                              newChatMember: ChatMember,
                              inviteLink: Option[ChatInviteLink]
                            )
