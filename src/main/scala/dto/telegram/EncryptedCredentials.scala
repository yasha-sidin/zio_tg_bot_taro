package ru.otus
package dto.telegram

import zio.json._

@jsonMemberNames(SnakeCase)
@jsonDerive
case class EncryptedCredentials(data: String, hash: String, secret: String)
