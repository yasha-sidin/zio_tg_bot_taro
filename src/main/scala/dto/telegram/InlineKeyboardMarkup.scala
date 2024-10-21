package ru.otus
package dto.telegram

object InlineKeyboardMarkup {}

case class InlineKeyboardMarkup(inlineKeyboard: Seq[Seq[InlineKeyboardButton]]) extends Markup
