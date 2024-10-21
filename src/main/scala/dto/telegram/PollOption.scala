package ru.otus
package dto.telegram

object PollOption {}

case class PollOption(text: String, voterCount: Long)
