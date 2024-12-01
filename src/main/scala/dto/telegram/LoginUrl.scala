package ru.otus
package dto.telegram

case class LoginUrl(
    url: String,
    forwardText: Option[String],
    botUsername: Option[String],
    requestWriteAccess: Option[Boolean]
)
