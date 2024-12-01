package ru.otus
package dto.telegram.`type`

import enumeratum.{EnumEntry, _}
import io.circe.{Decoder, Encoder}

sealed trait ChatBoostSource extends EnumEntry

object ChatBoostSource extends Enum[ChatBoostSource] {
  case object Premium  extends ChatBoostSource
  case object GiftCode extends ChatBoostSource
  case object Giveaway extends ChatBoostSource

  implicit val encoder: Encoder[ChatBoostSource] =
    Encoder.encodeString.contramap[ChatBoostSource] {
      case ChatBoostSource.Premium  => "premium"
      case ChatBoostSource.GiftCode => "gift_code"
      case ChatBoostSource.Giveaway => "giveaway"
    }

  implicit val decoder: Decoder[ChatBoostSource] = Decoder.decodeString.emap {
    case "premium"  => Right(ChatBoostSource.Premium)
    case "gift_code" => Right(ChatBoostSource.GiftCode)
    case "giveaway" => Right(ChatBoostSource.Giveaway)
    case other      => Left(s"Unknown ChatBoostSource: $other")
  }

  override def values: IndexedSeq[ChatBoostSource] = findValues
}
