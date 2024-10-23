package ru.otus
package dto.telegram

import zio.json._

@jsonMemberNames(SnakeCase)
@jsonDerive
case class KeyboardButton(
    text: String,
    requestContact: Option[Boolean],
    requestLocation: Option[Boolean],
    requestPoll: Option[KeyboardButtonPollType],
    webApp: Option[WebAppInfo]
)
