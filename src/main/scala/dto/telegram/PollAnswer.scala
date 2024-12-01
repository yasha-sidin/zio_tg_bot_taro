package ru.otus
package dto.telegram

case class PollAnswer(pollId: String, user: User, optionIds: Seq[Long])
