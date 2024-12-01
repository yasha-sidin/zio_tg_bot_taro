package ru.otus
package dto.telegram

case class PassportData(data: Seq[EncryptedPassportElement], credentials: EncryptedCredentials)
