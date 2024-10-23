package ru.otus
package dto.telegram

import zio.json._

@jsonMemberNames(SnakeCase)
@jsonDerive
case class VideoNote(
    fileId: String,
    fileUniqueId: String,
    length: Long,
    duration: Long,
    thumb: Option[PhotoSize],
    fileSize: Option[Long]
)
