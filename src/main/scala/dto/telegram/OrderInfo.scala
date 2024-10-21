package ru.otus
package dto.telegram

object OrderInfo {}

case class OrderInfo(
    name: Option[String],
    phoneNumber: Option[String],
    email: Option[String],
    shippingAddress: Option[ShippingAddress]
)
