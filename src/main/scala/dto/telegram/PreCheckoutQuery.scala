package ru.otus
package dto.telegram

case class PreCheckoutQuery(
    id: String,
    from: User,
    currency: String,
    totalAmount: Long,
    invoicePayload: String,
    shippingOptionId: Option[String],
    orderInfo: Option[OrderInfo]
)
