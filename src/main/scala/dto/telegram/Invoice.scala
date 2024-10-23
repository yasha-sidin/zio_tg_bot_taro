package ru.otus
package dto.telegram

import zio.json._

@jsonMemberNames(SnakeCase)
@jsonDerive
case class Invoice(
    title: String,
    description: String,
    startParameter: String,
    currency: String,
    totalAmount: Long
)
