package ru.otus
package util

import `type`.MainCommand
import dto.telegram.{KeyboardButton, ReplyKeyboardMarkup}

import zio.{UIO, ZIO}

object KeyboardUtil {
  def getMainKeyboard: UIO[ReplyKeyboardMarkup] = ZIO.succeed {
    ReplyKeyboardMarkup(
      keyboard = MainCommand.values.map(_.keyboardCommand).groupBy(_._2).toSeq.sortWith(_._1 > _._1).map(_._2.map(_._1)),
      resizeKeyboard = Some(true),
      oneTimeKeyboard = Some(false)
    )
  }

  def getStringKeyboardWithCancel(list: Seq[String]): UIO[ReplyKeyboardMarkup] = ZIO.succeed {
    ReplyKeyboardMarkup(
      keyboard = Seq(
        list.map(el => KeyboardButton(text = el)) :+ KeyboardButton(text = "Отмена")
      ),
      resizeKeyboard = Some(true),
      oneTimeKeyboard = Some(false)
    )
  }

  def getConfirmationKeyboard: UIO[ReplyKeyboardMarkup] = ZIO.succeed {
    ReplyKeyboardMarkup(
      keyboard = Seq(
        Seq(KeyboardButton(text = "Подтвердить"), KeyboardButton(text = "Отмена"))
      ),
      resizeKeyboard = Some(true),
      oneTimeKeyboard = Some(false)
    )
  }
}
