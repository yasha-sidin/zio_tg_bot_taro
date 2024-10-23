package ru.otus
package dto.telegram

import zio.json._

@jsonMemberNames(SnakeCase)
@jsonDerive
case class File(
    fileId: String,
    fileUniqueId: String,
    fileSize: Option[Long],
    filePath: Option[String]
)
