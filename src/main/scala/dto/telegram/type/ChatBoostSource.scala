package ru.otus
package dto.telegram.`type`

import enumeratum.{EnumEntry, _}

import zio.json._

@jsonDerive
sealed trait ChatBoostSource extends EnumEntry

object ChatBoostSource extends Enum[ChatBoostSource] {
  case object Premium  extends ChatBoostSource
  case object GiftCode extends ChatBoostSource
  case object Giveaway extends ChatBoostSource

  override def values: IndexedSeq[ChatBoostSource] = findValues
}
