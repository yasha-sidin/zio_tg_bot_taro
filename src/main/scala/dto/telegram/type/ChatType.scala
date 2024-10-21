package ru.otus
package dto.telegram

import enumeratum.{EnumEntry, _}

sealed trait ChatType extends EnumEntry

object ChatType extends Enum[ChatType] {
  case object Sender extends ChatType
  case object Private extends ChatType
  case object Group extends ChatType
  case object Supergroup extends ChatType
  case object Channel extends ChatType

  override def values: IndexedSeq[ChatType] = findValues

}