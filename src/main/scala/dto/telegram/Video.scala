package ru.otus
package dto.telegram

import zio.json._

@jsonMemberNames(SnakeCase)
@jsonDerive
case class Video(
    fileId: String,
    fileUniqueId: String,
    width: Long,
    height: Long,
    duration: Long,
    thumb: Option[PhotoSize],
    fileName: Option[String],
    mimeType: Option[String],
    fileSize: Option[Long]
)
