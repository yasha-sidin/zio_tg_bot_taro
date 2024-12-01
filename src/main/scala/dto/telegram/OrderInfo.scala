package ru.otus
package dto.telegram

case class OrderInfo(
    name: Option[String],
    phoneNumber: Option[String],
    email: Option[String],
    shippingAddress: Option[ShippingAddress]
)
