package ru.otus
package dto.telegram

import zio.json._

@jsonMemberNames(SnakeCase)
@jsonDerive
case class PollAnswer(pollId: String, user: User, optionIds: Seq[Long])
