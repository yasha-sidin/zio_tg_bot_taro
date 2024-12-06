package ru.otus
package `type`

import enumeratum.{EnumEntry, _}
import dto.telegram.BotCommand


sealed trait BotCommandType extends EnumEntry {
  def value: BotCommand
}

object BotCommandType extends Enum[BotCommandType] {
  case object Info extends BotCommandType {
    override def value: BotCommand = BotCommand("/about_me", "Обо мне")
  }

  case object Contacts extends BotCommandType {
    override def value: BotCommand = BotCommand("/contact", "Связаться со мной")
  }

  case object Enroll extends BotCommandType {
    override def value: BotCommand = BotCommand("/enroll", "Записаться на свободную дату")
  }


  override def values: IndexedSeq[BotCommandType] = findValues
}
