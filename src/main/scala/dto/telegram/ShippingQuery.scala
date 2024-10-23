package ru.otus
package dto.telegram

import zio.json._

@jsonMemberNames(SnakeCase)
@jsonDerive
case class ShippingQuery(id: String, from: User, invoicePayload: String, shippingAddress: ShippingAddress)