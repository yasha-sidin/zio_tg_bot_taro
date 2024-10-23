package ru.otus
package dto.telegram

import java.time.Instant

import zio.json._

@jsonMemberNames(SnakeCase)
@jsonDerive
case class PassportFile(fileId: String, fileUniqueId: String, fileSize: Long, fileDate: Instant)
