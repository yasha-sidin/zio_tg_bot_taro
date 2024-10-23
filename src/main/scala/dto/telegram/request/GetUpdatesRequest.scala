package ru.otus
package dto.telegram.request

object GetUpdatesRequest {}

import zio.json._

@jsonMemberNames(SnakeCase)
@jsonDerive
case class GetUpdatesRequest(
    offset: Option[Long],
    limit: Option[Long],
    timeout: Option[Long],
    allowedUpdates: Option[List[String]]
)
