package ru.otus
package dto.telegram

import zio.json._

@jsonMemberNames(SnakeCase)
@jsonDerive
case class ChosenInlineResult(
    resultId: String,
    from: User,
    location: Option[Location],
    inlineMessageId: Option[String],
    query: String
)
