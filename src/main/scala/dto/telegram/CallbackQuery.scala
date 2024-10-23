package ru.otus
package dto.telegram

import zio.json._

@jsonMemberNames(SnakeCase)
@jsonDerive
case class CallbackQuery(
    id: String,
    from: User,
    message: Option[Message],
    inlineMessageId: Option[String],
    chatInstance: String,
    data: Option[String],
    gameShortName: Option[String]
)
