package ru.otus
package dto.telegram

object InlineQuery {}

case class InlineQuery(
    id: String,
    from: User,
    query: String,
    offset: String,
    chatType: Option[ChatType],
    location: Option[Location]
)
