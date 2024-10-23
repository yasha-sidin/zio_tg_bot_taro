package ru.otus
package dto.telegram

import zio.json._

@jsonMemberNames(SnakeCase)
@jsonDerive
case class Voice(
    fileId: String,
    fileUniqueId: String,
    duration: Long,
    mimeType: Option[String],
    fileSize: Option[Int]
)
