package ru.otus
package dto.telegram

case class CallbackQuery(
    id: String,
    from: User,
    message: Option[Message],
    inlineMessageId: Option[String],
    chatInstance: String,
    data: Option[String],
    gameShortName: Option[String]
)
