package ru.otus
package dto.admin.`type`

import enumeratum.{EnumEntry, _}
import io.circe.{Decoder, Encoder}

sealed trait BookingStatus extends EnumEntry {
  def translation: String
}

object BookingStatus extends Enum[BookingStatus] {
  case object Active    extends BookingStatus {
    override def translation: String = "Активна"
  }
  case object Completed extends BookingStatus {
    override def translation: String = "Выполнена"
  }
  case object Cancelled extends BookingStatus {
    override def translation: String = "Отменена"
  }
  case object Confirmed extends BookingStatus {
    override def translation: String = "Подтверждена"
  }

  override def values: IndexedSeq[BookingStatus] = findValues

  implicit val encoder: Encoder[BookingStatus] =
    Encoder.encodeString.contramap[BookingStatus] {
      _.entryName
    }

  implicit val decoder: Decoder[BookingStatus] = Decoder.decodeString.emap { str =>
    BookingStatus.withNameOption(str).toRight(s"Invalid BookingStatus: $str")
  }
}
