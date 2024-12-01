package ru.otus
package dto.telegram

import dto.telegram.`type`.ChatBoostSource

case class ChatBoost(
    boostId: String,
    addDate: Long,
    expirationDate: Long,
    source: ChatBoostSource
)
