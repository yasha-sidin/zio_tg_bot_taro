package ru.otus
package `type`

import enumeratum.{EnumEntry, _}
import dto.telegram.{BotCommand, KeyboardButton}

sealed trait MainCommand extends EnumEntry  {
  def botCommand: BotCommand
  def keyboardCommand: (KeyboardButton, Int)
}

object MainCommand extends Enum[MainCommand]{
  case object Info extends MainCommand {
    override def botCommand: BotCommand = BotCommand("/about_me", "Обо мне")

    override def keyboardCommand: (KeyboardButton, Int) = (KeyboardButton(text = "Обо мне"), 1)
  }

  case object Contacts extends MainCommand {
    override def botCommand: BotCommand = BotCommand("/contact", "Связаться со мной")

    override def keyboardCommand: (KeyboardButton, Int) = (KeyboardButton(text = "Связаться со мной"), 1)
  }

  case object Enroll extends MainCommand {
    override def botCommand: BotCommand = BotCommand("/enroll", "Записаться на свободную дату")

    override def keyboardCommand: (KeyboardButton, Int) = (KeyboardButton(text = "\uD83D\uDD2E Записаться на свободную дату \uD83D\uDD2E"), 2)
  }

  case object MyBookings extends MainCommand {
    override def botCommand: BotCommand = BotCommand("/my-bookings", "Мои записи")

    override def keyboardCommand: (KeyboardButton, Int) = (KeyboardButton(text = "Мои записи"), 0)
  }

  case object CancelBooking extends MainCommand {
    override def botCommand: BotCommand = BotCommand("/cancel-booking", "Отменить запись")

    override def keyboardCommand: (KeyboardButton, Int) = (KeyboardButton(text = "Отменить запись"), 0)
  }

  override def values: IndexedSeq[MainCommand] = findValues
}
