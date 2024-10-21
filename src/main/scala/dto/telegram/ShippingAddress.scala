package ru.otus
package dto.telegram

object ShippingAddress {}

case class ShippingAddress(
    countryCode: String,
    state: String,
    city: String,
    streetLine1: String,
    streetLine2: String,
    postCode: String
)
