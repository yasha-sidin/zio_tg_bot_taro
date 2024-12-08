package ru.otus
package dto.telegram

case class KeyboardButton(
    text: String,
    requestContact: Option[Boolean] = None,
    requestLocation: Option[Boolean] = None,
    requestPoll: Option[KeyboardButtonPollType] = None,
    webApp: Option[WebAppInfo] = None
)
