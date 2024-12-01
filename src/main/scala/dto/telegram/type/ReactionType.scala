package ru.otus
package dto.telegram.`type`

import enumeratum.{EnumEntry, _}
import io.circe.{Decoder, Encoder}

sealed trait ReactionType extends EnumEntry

object ReactionType extends Enum[ReactionType] {
  case object Emoji       extends ReactionType
  case object CustomEmoji extends ReactionType
  case object Paid        extends ReactionType

  implicit val encoder: Encoder[ReactionType] = Encoder.encodeString.contramap[ReactionType] {
    case Emoji       => "emoji"
    case CustomEmoji => "custom_emoji"
    case Paid        => "paid"
  }

  // Создание Decoder для ReactionType
  implicit val decoder: Decoder[ReactionType] = Decoder.decodeString.emap {
    case "emoji"        => Right(Emoji)
    case "custom_emoji" => Right(CustomEmoji)
    case "paid"         => Right(Paid)
    case other          => Left(s"Unknown ReactionType: $other")
  }

  override def values: IndexedSeq[ReactionType] = findValues
}
