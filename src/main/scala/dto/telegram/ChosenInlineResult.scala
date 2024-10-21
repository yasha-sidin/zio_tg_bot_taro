package ru.otus
package dto.telegram

object ChosenInlineResult {}

case class ChosenInlineResult(
    resultId: String,
    from: User,
    location: Option[Location],
    inlineMessageId: Option[String],
    query: String
)
