package ru.otus
package dto.telegram

import zio.json._

@jsonMemberNames(SnakeCase)
@jsonDerive
case class PreCheckoutQuery(
    id: String,
    from: User,
    currency: String,
    totalAmount: Long,
    invoicePayload: String,
    shippingOptionId: Option[String],
    orderInfo: Option[OrderInfo]
)
