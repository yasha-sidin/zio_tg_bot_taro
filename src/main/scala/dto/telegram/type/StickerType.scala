package ru.otus
package dto.telegram.`type`

import enumeratum.{EnumEntry, _}
import io.circe.{Decoder, Encoder}

sealed trait StickerType extends EnumEntry

object StickerType extends Enum[StickerType] {
  case object Regular     extends StickerType
  case object Mask        extends StickerType
  case object CustomEmoji extends StickerType

  implicit val encoder: Encoder[StickerType] = Encoder.encodeString.contramap[StickerType] {
    case Regular     => "regular"
    case Mask        => "mask"
    case CustomEmoji => "custom_emoji"
  }

  // Создание Decoder для StickerType
  implicit val decoder: Decoder[StickerType] = Decoder.decodeString.emap {
    case "regular"      => Right(Regular)
    case "mask"         => Right(Mask)
    case "custom_emoji" => Right(CustomEmoji)
    case other          => Left(s"Unknown StickerType: $other")
  }

  override def values: IndexedSeq[StickerType] = findValues
}
