package ru.otus
package `type`

import enumeratum.{EnumEntry, _}
import io.getquill.MappedEncoding

sealed trait ChatStateType extends EnumEntry

object ChatStateType extends Enum[ChatStateType] {
  case object MainMenu extends ChatStateType

  case object MonthSelection extends ChatStateType

  case object DaySelection extends ChatStateType

  case object TimeSelection extends ChatStateType

  case object Confirmation extends ChatStateType

  override def values: IndexedSeq[ChatStateType] = findValues

  implicit val encodeUserStatus: MappedEncoding[ChatStateType, String] =
    MappedEncoding[ChatStateType, String](_.entryName)

  implicit val decodeUserStatus: MappedEncoding[String, ChatStateType] =
    MappedEncoding[String, ChatStateType](ChatStateType.withName)
}
