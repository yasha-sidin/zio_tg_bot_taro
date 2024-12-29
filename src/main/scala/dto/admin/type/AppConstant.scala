package ru.otus
package dto.admin.`type`

import enumeratum.{EnumEntry, _}
import io.circe.{Decoder, Encoder}

sealed trait AppConstant extends EnumEntry {
  def serialize: Any => String
  def deserialize: String => Any
}

object AppConstant extends Enum[AppConstant] {
  case object MaxTimeToConfirm extends AppConstant {
    override def serialize: Any => String    = data => data.toString
    override def deserialize: String => Long = str => str.toLong
  }

  case object AboutMe extends AppConstant {
    override def serialize: Any => String = data => data.toString
    override def deserialize: String => String = str => str
  }

  case object MyContacts extends AppConstant {
    override def serialize: Any => String = data => data.toString
    override def deserialize: String => String = str => str
  }

  override def values: IndexedSeq[AppConstant] = findValues

  implicit val encoder: Encoder[AppConstant] =
    Encoder.encodeString.contramap[AppConstant] {
      _.entryName
    }

  implicit val decoder: Decoder[AppConstant] = Decoder.decodeString.emap { str =>
    AppConstant.withNameOption(str).toRight(s"Invalid BookingStatus: $str")
  }
}
