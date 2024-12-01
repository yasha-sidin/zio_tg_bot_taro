package ru.otus
package dto.telegram

case class File(
    fileId: String,
    fileUniqueId: String,
    fileSize: Option[Long],
    filePath: Option[String]
)
