package ru.otus
package dto.telegram

object ReplyKeyboardMarkup {}

case class ReplyKeyboardMarkup(
    keyboard: Seq[Seq[KeyboardButton]],
    isPersistent: Option[Boolean],
    resizeKeyboard: Option[Boolean],
    oneTimeKeyboard: Option[Boolean],
    inputFieldPlaceholder: Option[String],
    selective: Option[Boolean]
) extends Markup
