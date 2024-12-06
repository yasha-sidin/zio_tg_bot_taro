package ru.otus
package dto.telegram.request

object GetUpdatesRequest {}

case class GetUpdatesRequest(
    offset: Option[Long] = None,
    limit: Option[Long] = None,
    timeout: Option[Long] = None,
    allowedUpdates: Option[List[String]] = None
)
