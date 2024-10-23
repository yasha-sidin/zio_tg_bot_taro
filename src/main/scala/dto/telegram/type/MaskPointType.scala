package ru.otus
package dto.telegram.`type`

import enumeratum.{EnumEntry, _}
import zio.json.jsonDerive

@jsonDerive
sealed trait MaskPointType extends EnumEntry

object MaskPointType extends Enum[MaskPointType] {
  case object Forehead extends MaskPointType
  case object Eyes extends MaskPointType
  case object Mouth extends MaskPointType
  case object Chin extends MaskPointType

  override def values: IndexedSeq[MaskPointType] = findValues
}