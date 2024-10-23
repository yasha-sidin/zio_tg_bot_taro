package ru.otus
package dto.telegram

import dto.telegram.`type`.ChatType
import zio.json._

@jsonMemberNames(SnakeCase)
@jsonDerive
case class InlineQuery(
    id: String,
    from: User,
    query: String,
    offset: String,
    chatType: Option[ChatType],
    location: Option[Location]
)
