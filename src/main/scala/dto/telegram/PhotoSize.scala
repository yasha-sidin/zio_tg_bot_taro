package ru.otus
package dto.telegram

case class PhotoSize(
    fileId: String,
    fileUniqueId: String,
    width: Long,
    height: Long,
    fileSize: Option[Long]
)
