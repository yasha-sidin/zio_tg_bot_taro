package ru.otus
package dto.telegram

object PollAnswer {

}

case class PollAnswer(pollId: String, user: User, optionIds: Seq[Long])
