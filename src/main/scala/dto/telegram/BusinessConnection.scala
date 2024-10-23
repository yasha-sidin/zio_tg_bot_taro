package ru.otus
package dto.telegram

import zio.json._

@jsonMemberNames(SnakeCase)
@jsonDerive
case class BusinessConnection(
    id: String,
    user: User,
    userChatId: Long,
    date: Long,
    canReply: Boolean,
    isEnabled: Boolean
)
