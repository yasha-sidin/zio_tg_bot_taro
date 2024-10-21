package ru.otus
package dto.telegram.request

object GetUpdatesRequest {}

case class GetUpdatesRequest(
    offset: Option[Long],
    limit: Option[Long],
    timeout: Option[Long],
    allowedUpdates: Option[List[String]]
)
