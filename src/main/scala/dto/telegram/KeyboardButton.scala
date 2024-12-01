package ru.otus
package dto.telegram

case class KeyboardButton(
    text: String,
    requestContact: Option[Boolean],
    requestLocation: Option[Boolean],
    requestPoll: Option[KeyboardButtonPollType],
    webApp: Option[WebAppInfo]
)
