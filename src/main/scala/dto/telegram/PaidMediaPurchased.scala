package ru.otus
package dto.telegram

import zio.json._

@jsonMemberNames(SnakeCase)
@jsonDerive
case class PaidMediaPurchased(
    from: User,
    paidMediaPayload: String
)
