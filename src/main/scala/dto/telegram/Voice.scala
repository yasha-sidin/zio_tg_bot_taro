package ru.otus
package dto.telegram

case class Voice(
    fileId: String,
    fileUniqueId: String,
    duration: Long,
    mimeType: Option[String],
    fileSize: Option[Int]
)
