package ru.otus
package dto.telegram

case class ChosenInlineResult(
    resultId: String,
    from: User,
    location: Option[Location],
    inlineMessageId: Option[String],
    query: String
)
