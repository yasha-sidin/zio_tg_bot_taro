package ru.otus
package dto.telegram

import zio.json._

@jsonMemberNames(SnakeCase)
@jsonDerive
case class PhotoSize(
    fileId: String,
    fileUniqueId: String,
    width: Long,
    height: Long,
    fileSize: Option[Long]
)
