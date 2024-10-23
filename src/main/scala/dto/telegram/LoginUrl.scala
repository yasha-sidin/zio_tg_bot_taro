package ru.otus
package dto.telegram

import zio.json._

@jsonMemberNames(SnakeCase)
@jsonDerive
case class LoginUrl(
    url: String,
    forwardText: Option[String],
    botUsername: Option[String],
    requestWriteAccess: Option[Boolean]
)
