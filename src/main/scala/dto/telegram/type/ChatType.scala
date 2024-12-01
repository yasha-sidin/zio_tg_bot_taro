package ru.otus
package dto.telegram.`type`

import enumeratum.{EnumEntry, _}
import io.circe.{Decoder, Encoder}

sealed trait ChatType extends EnumEntry

object ChatType extends Enum[ChatType] {
  case object Sender     extends ChatType
  case object Private    extends ChatType
  case object Group      extends ChatType
  case object Supergroup extends ChatType
  case object Channel    extends ChatType

  implicit val encoder: Encoder[ChatType] = Encoder.encodeString.contramap[ChatType] {
    case ChatType.Sender     => "sender"
    case ChatType.Private    => "private"
    case ChatType.Group      => "group"
    case ChatType.Supergroup => "supergroup"
    case ChatType.Channel    => "channel"
  }

  implicit val decoder: Decoder[ChatType] = Decoder.decodeString.emap {
    case "sender"     => Right(ChatType.Sender)
    case "private"    => Right(ChatType.Private)
    case "group"      => Right(ChatType.Group)
    case "supergroup" => Right(ChatType.Supergroup)
    case "channel"    => Right(ChatType.Channel)
    case other        => Left(s"Unknown ChatType: $other")
  }

  override def values: IndexedSeq[ChatType] = findValues
}
