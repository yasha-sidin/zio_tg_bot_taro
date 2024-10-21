package ru.otus
package dto.telegram

object KeyboardButton {}

case class KeyboardButton(
    text: String,
    requestContact: Option[Boolean],
    requestLocation: Option[Boolean],
    requestPoll: Option[KeyboardButtonPollType],
    webApp: Option[WebAppInfo]
)
