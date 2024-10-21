package ru.otus
package dto.telegram

import enumeratum.{EnumEntry, _}

sealed trait ChatBoostSource extends EnumEntry

object ChatBoostSource extends Enum[ChatBoostSource] {
  case object Premium extends ChatBoostSource
  case object GiftCode extends ChatBoostSource
  case object Giveaway extends ChatBoostSource

  override def values: IndexedSeq[ChatBoostSource] = findValues
}
