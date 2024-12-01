package ru.otus
package dto.telegram

case class PaidMediaPurchased(
    from: User,
    paidMediaPayload: String
)
