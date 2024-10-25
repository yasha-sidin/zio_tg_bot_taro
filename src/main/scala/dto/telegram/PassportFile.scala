package ru.otus
package dto.telegram

import zio.json._

@jsonMemberNames(SnakeCase)
@jsonDerive
case class PassportFile(fileId: String, fileUniqueId: String, fileSize: Long, fileDate: Long)
