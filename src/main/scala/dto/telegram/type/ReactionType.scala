package ru.otus
package dto.telegram.`type`

import enumeratum.EnumEntry

import zio.json._

@jsonDerive
sealed trait ReactionType extends EnumEntry

object ReactionType {
  case object Emoji       extends ReactionType
  case object CustomEmoji extends ReactionType
  case object Paid        extends ReactionType
}
