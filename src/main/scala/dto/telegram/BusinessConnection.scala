package ru.otus
package dto.telegram

object BusinessConnection {}

case class BusinessConnection(
    id: String,
    user: User,
    userChatId: Long,
    date: Long,
    canReply: Boolean,
    isEnabled: Boolean
)