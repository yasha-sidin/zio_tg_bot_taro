package ru.otus
package dto.telegram.`type`

import enumeratum.{EnumEntry, _}

import zio.json._

@jsonDerive
sealed trait PollType extends EnumEntry

object PollType extends Enum[PollType] {
  case object Regular extends PollType
  case object Quiz    extends PollType

  override def values: IndexedSeq[PollType] = findValues

}
