package ru.otus
package dto.telegram

case class ShippingQuery(id: String, from: User, invoicePayload: String, shippingAddress: ShippingAddress)