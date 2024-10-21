package ru.otus
package dto.telegram

import enumeratum.EnumEntry

sealed trait ReactionType extends EnumEntry

object ReactionType {
  case object Emoji extends ReactionType
  case object CustomEmoji extends ReactionType
  case object Paid extends ReactionType
}
