package ru.otus
package dto.telegram

import zio.json._

@jsonMemberNames(SnakeCase)
@jsonDerive
case class TelegramResponse[T](ok: Boolean, result: List[T])
