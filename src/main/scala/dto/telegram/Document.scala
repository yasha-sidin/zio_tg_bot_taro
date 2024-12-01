package ru.otus
package dto.telegram

case class Document(
    fileId: String,
    fileUniqueId: String,
    thumb: Option[PhotoSize],
    fileName: Option[String],
    mimeType: Option[String],
    fileSize: Option[Long]
)
