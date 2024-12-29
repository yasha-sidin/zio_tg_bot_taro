package ru.otus
package dto.admin.`type`

import enumeratum.{EnumEntry, _}
import io.circe.{Decoder, Encoder}

sealed trait AppointmentDateStatus extends EnumEntry

object AppointmentDateStatus extends Enum[AppointmentDateStatus] {
  case object Available extends AppointmentDateStatus
  case object Booked    extends AppointmentDateStatus
  case object Expired   extends AppointmentDateStatus

  override def values: IndexedSeq[AppointmentDateStatus] = findValues

  implicit val encoder: Encoder[AppointmentDateStatus] =
    Encoder.encodeString.contramap[AppointmentDateStatus] {
      _.entryName
    }

  implicit val decoder: Decoder[AppointmentDateStatus] = Decoder.decodeString.emap { str =>
    AppointmentDateStatus.withNameOption(str).toRight(s"Invalid BookingStatus: $str")
  }
}
