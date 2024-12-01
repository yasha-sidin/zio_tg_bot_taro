package ru.otus
package dto.telegram

case class Contact(
    phoneNumber: String,
    firstName: String,
    lastName: Option[String],
    userId: Option[Long],
    vcard: Option[String]
)
