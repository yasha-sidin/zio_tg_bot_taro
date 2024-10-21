package ru.otus
package dto.telegram

object MessageEntity {}

case class MessageEntity(
    `type`: MessageEntityType,
    offset: Long,
    length: Long,
    url: Option[String],
    user: Option[User],
    language: Option[String],
    customEmojiId: Option[String]
)
