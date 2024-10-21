package ru.otus
package dto.telegram

object CallbackQuery {}

case class CallbackQuery(
    id: String,
    from: User,
    message: Option[Message],
    inlineMessageId: Option[String],
    chatInstance: String,
    data: Option[String],
    gameShortName: Option[String]
)
