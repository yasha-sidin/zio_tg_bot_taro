package ru.otus
package dto.telegram.`type`

import enumeratum.{EnumEntry, _}
import io.circe.{Decoder, Encoder}

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

  implicit val encoder: Encoder[PassportElementType] =
    Encoder.encodeString.contramap[PassportElementType] {
      case PersonalDetails       => "personal_details"
      case Passport              => "passport"
      case DriverLicense         => "driver_license"
      case IdentityCard          => "identity_card"
      case InternalPassport      => "internal_passport"
      case Address               => "address"
      case UtilityBill           => "utility_bill"
      case BankStatement         => "bank_statement"
      case RentalAgreement       => "rental_agreement"
      case PassportRegistration  => "passport_registration"
      case TemporaryRegistration => "temporary_registration"
      case PhoneNumber           => "phone_number"
      case Email                 => "email"
    }

  implicit val decoder: Decoder[PassportElementType] = Decoder.decodeString.emap {
    case "personal_details"       => Right(PersonalDetails)
    case "passport"               => Right(Passport)
    case "driver_license"         => Right(DriverLicense)
    case "identity_card"          => Right(IdentityCard)
    case "internal_passport"      => Right(InternalPassport)
    case "address"                => Right(Address)
    case "utility_bill"           => Right(UtilityBill)
    case "bank_statement"         => Right(BankStatement)
    case "rental_agreement"       => Right(RentalAgreement)
    case "passport_registration"  => Right(PassportRegistration)
    case "temporary_registration" => Right(TemporaryRegistration)
    case "phone_number"           => Right(PhoneNumber)
    case "email"                  => Right(Email)
    case other                    => Left(s"Unknown PassportElementType: $other")
  }

  override def values: IndexedSeq[PassportElementType] = findValues
}
