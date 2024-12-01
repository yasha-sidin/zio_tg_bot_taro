package ru.otus
package dto.telegram

import dto.telegram.`type`.PassportElementType

case class EncryptedPassportElement(
    `type`: PassportElementType,
    data: Option[String],
    phoneNumber: Option[String],
    email: Option[String],
    files: Option[Seq[PassportFile]],
    frontSide: Option[PassportFile],
    reverseSide: Option[PassportFile],
    selfie: Option[PassportFile],
    translation: Option[Seq[PassportFile]],
    hash: String
)
