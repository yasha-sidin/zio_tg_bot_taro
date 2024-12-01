package ru.otus
package dto.telegram.`type`

import enumeratum.{EnumEntry, _}
import io.circe.{Decoder, Encoder}

sealed trait PollType extends EnumEntry

object PollType extends Enum[PollType] {
  case object Regular extends PollType
  case object Quiz    extends PollType

  implicit val encoder: Encoder[PollType] = Encoder.encodeString.contramap[PollType] {
    case Regular => "regular"
    case Quiz    => "quiz"
  }

  implicit val decoder: Decoder[PollType] = Decoder.decodeString.emap {
    case "regular" => Right(Regular)
    case "quiz"    => Right(Quiz)
    case other     => Left(s"Unknown PollType: $other")
  }

  override def values: IndexedSeq[PollType] = findValues
}
