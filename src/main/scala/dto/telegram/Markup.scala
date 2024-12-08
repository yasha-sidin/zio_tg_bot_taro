package ru.otus
package dto.telegram

import io.circe.Encoder
import io.circe.generic.extras.auto._
import configuration.CirceConfig._
import io.circe.syntax._

sealed trait Markup

case class ForceReply(
    forceReply: Boolean,
    inputFieldPlaceholder: Option[String],
    selective: Option[Boolean]
) extends Markup

case class InlineKeyboardMarkup(inlineKeyboard: Seq[Seq[InlineKeyboardButton]]) extends Markup

case class ReplyKeyboardMarkup(
    keyboard: Seq[Seq[KeyboardButton]],
    isPersistent: Option[Boolean] = None,
    resizeKeyboard: Option[Boolean] = None,
    oneTimeKeyboard: Option[Boolean] = None,
    inputFieldPlaceholder: Option[String] = None,
    selective: Option[Boolean] = None
) extends Markup

object Markup {
  implicit val encoder: Encoder[Markup] = Encoder.instance {
    case rkm: ReplyKeyboardMarkup => rkm.asJson
    case rkr: ReplyKeyboardRemove => rkr.asJson
    case fr: ForceReply => fr.asJson
    case ikm: InlineKeyboardMarkup => ikm.asJson
  }
}

case class ReplyKeyboardRemove(removeKeyboard: Boolean, selective: Option[Boolean]) extends Markup
