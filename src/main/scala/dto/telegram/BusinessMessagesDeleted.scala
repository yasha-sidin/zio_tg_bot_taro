package ru.otus
package dto.telegram

import zio.json._

@jsonMemberNames(SnakeCase)
@jsonDerive
case class BusinessMessagesDeleted(
    businessConnectionId: String,
    chat: Chat,
    messageIds: List[Long]
)
