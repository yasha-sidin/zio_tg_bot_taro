package ru.otus
package dto.telegram

case class VideoNote(
    fileId: String,
    fileUniqueId: String,
    length: Long,
    duration: Long,
    thumb: Option[PhotoSize],
    fileSize: Option[Long]
)
