package ru.otus
package dto.admin

case class TelegramDetails(
    chatId: Long,
    firstName: String,
    lastName: Option[String],
    username: Option[String],
    languageCode: Option[String],
  )
