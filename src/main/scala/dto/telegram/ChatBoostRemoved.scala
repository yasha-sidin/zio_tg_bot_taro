package ru.otus
package dto.telegram

import dto.telegram.`type`.ChatBoostSource
import zio.json._

@jsonMemberNames(SnakeCase)
@jsonDerive
case class ChatBoostRemoved(
    chat: Chat,
    boostId: String,
    removeDate: Long,
    source: ChatBoostSource
)
