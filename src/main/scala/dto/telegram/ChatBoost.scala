package ru.otus
package dto.telegram

import java.time.Instant

object ChatBoost {}

case class ChatBoost(
    boostId: String,
    addDate: Instant,
    expirationDate: Instant,
    source: ChatBoostSource
)
