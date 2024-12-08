package ru.otus
package dto.telegram

case class InlineKeyboardButton(
    text: String,
    url: Option[String] = None,
    callbackData: Option[String] = None,
    webApp: Option[WebAppInfo] = None,
    loginUrl: Option[LoginUrl] = None,
    switchInlineQuery: Option[String] = None,
    switchInlineQueryCurrentChat: Option[String] = None,
    callbackGame: Option[CallbackGame] = None,
    pay: Option[Boolean] = None
)
