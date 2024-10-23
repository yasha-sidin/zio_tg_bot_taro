package ru.otus
package dto.telegram

import zio.json._

@jsonMemberNames(SnakeCase)
@jsonDerive
case class Contact(
    phoneNumber: String,
    firstName: String,
    lastName: Option[String],
    userId: Option[Long],
    vcard: Option[String]
)
