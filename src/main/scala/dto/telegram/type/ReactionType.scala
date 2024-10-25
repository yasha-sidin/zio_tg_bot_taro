package ru.otus
package dto.telegram.`type`

import enumeratum.{EnumEntry, _}
import zio.json._

@jsonDerive
sealed trait ReactionType extends EnumEntry

object ReactionType extends Enum[ReactionType] {
  case object Emoji       extends ReactionType
  case object CustomEmoji extends ReactionType
  case object Paid        extends ReactionType

  override def values: IndexedSeq[ReactionType] = findValues
}
