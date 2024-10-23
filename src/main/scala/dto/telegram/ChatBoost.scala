package ru.otus
package dto.telegram

import dto.telegram.`type`.ChatBoostSource

import java.time.Instant
import zio.json._

@jsonMemberNames(SnakeCase)
@jsonDerive
case class ChatBoost(
    boostId: String,
    addDate: Instant,
    expirationDate: Instant,
    source: ChatBoostSource
)
