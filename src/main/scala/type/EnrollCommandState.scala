package ru.otus
package `type`

import enumeratum.{EnumEntry, _}
import io.getquill.MappedEncoding

sealed trait EnrollCommandState extends EnumEntry

object EnrollCommandState extends Enum[EnrollCommandState] {
  case object MonthSelection extends EnrollCommandState

  case object DaySelection extends EnrollCommandState

  case object TimeSelection extends EnrollCommandState

  case object Confirmation extends EnrollCommandState

  override def values: IndexedSeq[EnrollCommandState] = findValues

  implicit val encodeUserStatus: MappedEncoding[EnrollCommandState, String] =
    MappedEncoding[EnrollCommandState, String](_.entryName)

  implicit val decodeUserStatus: MappedEncoding[String, EnrollCommandState] =
    MappedEncoding[String, EnrollCommandState](EnrollCommandState.withName)
}
