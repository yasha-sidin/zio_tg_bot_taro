package ru.otus
package dto.telegram.`type`

import enumeratum.{EnumEntry, _}

import zio.json._

@jsonDerive
sealed trait PassportElementType extends EnumEntry

object PassportElementType extends Enum[PassportElementType] {
  case object PersonalDetails       extends PassportElementType
  case object Passport              extends PassportElementType
  case object DriverLicense         extends PassportElementType
  case object IdentityCard          extends PassportElementType
  case object InternalPassport      extends PassportElementType
  case object Address               extends PassportElementType
  case object UtilityBill           extends PassportElementType
  case object BankStatement         extends PassportElementType
  case object RentalAgreement       extends PassportElementType
  case object PassportRegistration  extends PassportElementType
  case object TemporaryRegistration extends PassportElementType
  case object PhoneNumber           extends PassportElementType
  case object Email                 extends PassportElementType

  override def values: IndexedSeq[PassportElementType] = findValues

}
