package ru.otus
package dto.telegram

import zio.json._

@jsonMemberNames(SnakeCase)
@jsonDerive
case class InlineKeyboardButton(
    text: String,
    url: Option[String],
    callbackData: Option[String],
    webApp: Option[WebAppInfo],
    loginUrl: Option[LoginUrl],
    switchInlineQuery: Option[String],
    switchInlineQueryCurrentChat: Option[String],
    callbackGame: Option[CallbackGame],
    pay: Option[Boolean]
)
