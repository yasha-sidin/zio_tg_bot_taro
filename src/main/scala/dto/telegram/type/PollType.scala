package ru.otus
package dto.telegram

import enumeratum.{EnumEntry, _}

sealed trait PollType extends EnumEntry

object PollType extends Enum[PollType] {
  case object Regular extends PollType
  case object Quiz extends PollType

  override def values: IndexedSeq[PollType] = findValues

}
