package ru.otus
package dto.telegram

import enumeratum.{EnumEntry, _}

sealed trait MessageEntityType extends EnumEntry

object MessageEntityType extends Enum[MessageEntityType] {
  case object Mention extends MessageEntityType
  case object Hashtag extends MessageEntityType
  case object Cashtag extends MessageEntityType
  case object BotCommand extends MessageEntityType
  case object Url extends MessageEntityType
  case object Email extends MessageEntityType
  case object PhoneNumber extends MessageEntityType
  case object Bold extends MessageEntityType
  case object Italic extends MessageEntityType
  case object Underline extends MessageEntityType
  case object Strikethrough extends MessageEntityType
  case object Spoiler extends MessageEntityType
  case object Code extends MessageEntityType
  case object Pre extends MessageEntityType
  case object TextLink extends MessageEntityType
  case object TextMention extends MessageEntityType
  case object CustomEmoji extends MessageEntityType

  override def values: IndexedSeq[MessageEntityType] = findValues


}