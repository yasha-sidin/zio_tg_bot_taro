package ru.otus
package dto.telegram

sealed trait Markup

case class ForceReply(
    forceReply: Boolean,
    inputFieldPlaceholder: Option[String],
    selective: Option[Boolean]
) extends Markup

case class InlineKeyboardMarkup(inlineKeyboard: Seq[Seq[InlineKeyboardButton]]) extends Markup

case class ReplyKeyboardMarkup(
    keyboard: Seq[Seq[KeyboardButton]],
    isPersistent: Option[Boolean],
    resizeKeyboard: Option[Boolean],
    oneTimeKeyboard: Option[Boolean],
    inputFieldPlaceholder: Option[String],
    selective: Option[Boolean]
) extends Markup

case class ReplyKeyboardRemove(removeKeyboard: Boolean, selective: Option[Boolean]) extends Markup
