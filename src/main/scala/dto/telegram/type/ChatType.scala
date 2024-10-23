package ru.otus
package dto.telegram.`type`

import enumeratum.{EnumEntry, _}

import zio.json._

@jsonDerive
sealed trait ChatType extends EnumEntry

object ChatType extends Enum[ChatType] {
  case object Sender     extends ChatType
  case object Private    extends ChatType
  case object Group      extends ChatType
  case object Supergroup extends ChatType
  case object Channel    extends ChatType

  override def values: IndexedSeq[ChatType] = findValues

}
