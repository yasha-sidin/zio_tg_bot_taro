package ru.otus
package dto.telegram.`type`

import enumeratum.{EnumEntry, _}
import io.circe.{Decoder, Encoder}

sealed trait MaskPointType extends EnumEntry

object MaskPointType extends Enum[MaskPointType] {
  case object Forehead extends MaskPointType
  case object Eyes     extends MaskPointType
  case object Mouth    extends MaskPointType
  case object Chin     extends MaskPointType

  implicit val encoder: Encoder[MaskPointType] =
    Encoder.encodeString.contramap[MaskPointType] {
      case MaskPointType.Forehead => "forehead"
      case MaskPointType.Eyes     => "eyes"
      case MaskPointType.Mouth    => "mouth"
      case MaskPointType.Chin     => "chin"
    }

  implicit val decoder: Decoder[MaskPointType] = Decoder.decodeString.emap {
    case "forehead" => Right(MaskPointType.Forehead)
    case "eyes"     => Right(MaskPointType.Eyes)
    case "mouth"    => Right(MaskPointType.Mouth)
    case "chin"     => Right(MaskPointType.Chin)
    case other      => Left(s"Unknown MaskPointType: $other")
  }

  override def values: IndexedSeq[MaskPointType] = findValues
}
