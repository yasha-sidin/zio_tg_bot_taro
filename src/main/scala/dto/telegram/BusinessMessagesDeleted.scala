package ru.otus
package dto.telegram

object BusinessMessagesDeleted {}

case class BusinessMessagesDeleted(
    businessConnectionId: String,
    chat: Chat,
    messageIds: List[Long]
)
