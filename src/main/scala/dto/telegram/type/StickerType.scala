package ru.otus
package dto.telegram.`type`

import enumeratum.{EnumEntry, _}

import zio.json._

@jsonDerive
sealed trait StickerType extends EnumEntry

object StickerType extends Enum[StickerType] {
  case object Regular     extends StickerType
  case object Mask        extends StickerType
  case object CustomEmoji extends StickerType

  override def values: IndexedSeq[StickerType] = findValues
}
