package ru.otus
package dto.telegram

import dto.telegram.`type`.ChatBoostSource

import zio.json._

@jsonMemberNames(SnakeCase)
@jsonDerive
case class ChatBoost(
    boostId: String,
    addDate: Long,
    expirationDate: Long,
    source: ChatBoostSource
)
