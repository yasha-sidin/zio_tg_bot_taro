package ru.otus
package dto.telegram

case class Audio(
    fileId: String,
    fileUniqueId: String,
    duration: Long,
    performer: Option[String],
    title: Option[String],
    fileName: Option[String],
    mimeType: Option[String],
    fileSize: Option[Long],
    thumb: Option[PhotoSize]
)
