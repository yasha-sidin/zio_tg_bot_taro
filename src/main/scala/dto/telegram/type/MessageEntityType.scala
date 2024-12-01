package ru.otus
package dto.telegram.`type`

import enumeratum.{EnumEntry, _}
import io.circe.{Decoder, Encoder}

sealed trait MessageEntityType extends EnumEntry

object MessageEntityType extends Enum[MessageEntityType] {
  case object Mention       extends MessageEntityType
  case object Hashtag       extends MessageEntityType
  case object Cashtag       extends MessageEntityType
  case object BotCommand    extends MessageEntityType
  case object Url           extends MessageEntityType
  case object Email         extends MessageEntityType
  case object PhoneNumber   extends MessageEntityType
  case object Bold          extends MessageEntityType
  case object Italic        extends MessageEntityType
  case object Underline     extends MessageEntityType
  case object Strikethrough extends MessageEntityType
  case object Spoiler       extends MessageEntityType
  case object Code          extends MessageEntityType
  case object Pre           extends MessageEntityType
  case object TextLink      extends MessageEntityType
  case object TextMention   extends MessageEntityType
  case object CustomEmoji   extends MessageEntityType

  implicit val encoder: Encoder[MessageEntityType] =
    Encoder.encodeString.contramap[MessageEntityType] {
      case MessageEntityType.Mention       => "mention"
      case MessageEntityType.Hashtag       => "hashtag"
      case MessageEntityType.Cashtag       => "cashtag"
      case MessageEntityType.BotCommand    => "bot_command"
      case MessageEntityType.Url           => "url"
      case MessageEntityType.Email         => "email"
      case MessageEntityType.PhoneNumber   => "phone_number"
      case MessageEntityType.Bold          => "bold"
      case MessageEntityType.Italic        => "italic"
      case MessageEntityType.Underline     => "underline"
      case MessageEntityType.Strikethrough => "strikethrough"
      case MessageEntityType.Spoiler       => "spoiler"
      case MessageEntityType.Code          => "code"
      case MessageEntityType.Pre           => "pre"
      case MessageEntityType.TextLink      => "text_link"
      case MessageEntityType.TextMention   => "text_mention"
      case MessageEntityType.CustomEmoji   => "custom_emoji"
    }

  implicit val decoder: Decoder[MessageEntityType] = Decoder.decodeString.emap {
    case "mention"       => Right(Mention)
    case "hashtag"       => Right(Hashtag)
    case "cashtag"       => Right(Cashtag)
    case "bot_command"   => Right(BotCommand)
    case "url"           => Right(Url)
    case "email"         => Right(Email)
    case "phone_number"  => Right(PhoneNumber)
    case "bold"          => Right(Bold)
    case "italic"        => Right(Italic)
    case "underline"     => Right(Underline)
    case "strikethrough" => Right(Strikethrough)
    case "spoiler"       => Right(Spoiler)
    case "code"          => Right(Code)
    case "pre"           => Right(Pre)
    case "text_link"     => Right(TextLink)
    case "text_mention"  => Right(TextMention)
    case "custom_emoji"  => Right(CustomEmoji)
    case other           => Left(s"Unknown MessageEntityType: $other")
  }

  override def values: IndexedSeq[MessageEntityType] = findValues
}
