package ru.otus
package dto.telegram

import dto.telegram.`type`.MessageEntityType

case class MessageEntity(
    `type`: MessageEntityType,
    offset: Long,
    length: Long,
    url: Option[String],
    user: Option[User],
    language: Option[String],
    customEmojiId: Option[String]
)
