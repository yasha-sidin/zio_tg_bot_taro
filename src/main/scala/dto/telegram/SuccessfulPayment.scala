package ru.otus
package dto.telegram

import zio.json._

@jsonMemberNames(SnakeCase)
@jsonDerive
case class SuccessfulPayment(
    currency: String,
    totalAmount: Long,
    invoicePayload: String,
    shippingOptionId: Option[String],
    orderInfo: Option[OrderInfo],
    telegramPaymentChargeId: String,
    providerPaymentChargeId: String
)
