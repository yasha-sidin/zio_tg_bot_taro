package ru.otus
package dto.telegram

import zio.json._

@jsonMemberNames(SnakeCase)
@jsonDerive
case class Document(
    fileId: String,
    fileUniqueId: String,
    thumb: Option[PhotoSize],
    fileName: Option[String],
    mimeType: Option[String],
    fileSize: Option[Long]
)
