package ru.otus
package dto.telegram

import zio.json._

@jsonMemberNames(SnakeCase)
@jsonDerive
case class OrderInfo(
    name: Option[String],
    phoneNumber: Option[String],
    email: Option[String],
    shippingAddress: Option[ShippingAddress]
)
