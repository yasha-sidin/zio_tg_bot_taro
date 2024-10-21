package ru.otus
package dto.telegram

object ShippingQuery {

}

case class ShippingQuery(id: String, from: User, invoicePayload: String, shippingAddress: ShippingAddress)