package ru.otus
package dto.telegram

import dto.telegram.`type`.PollType
import zio.json._

@jsonMemberNames(SnakeCase)
@jsonDerive
case class KeyboardButtonPollType(`type`: PollType)
