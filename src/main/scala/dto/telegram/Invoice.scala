package ru.otus
package dto.telegram

import zio.json._

case class Invoice(
    title: String,
    description: String,
    startParameter: String,
    currency: String,
    totalAmount: Long
)
