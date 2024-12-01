package ru.otus
package dto.telegram

case class BusinessMessagesDeleted(
    businessConnectionId: String,
    chat: Chat,
    messageIds: List[Long]
)
