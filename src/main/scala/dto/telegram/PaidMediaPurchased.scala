package ru.otus
package dto.telegram

object PaidMediaPurchased {}

case class PaidMediaPurchased(
    from: User,
    paidMediaPayload: String
)
