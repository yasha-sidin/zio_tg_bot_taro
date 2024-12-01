package ru.otus
package dto.telegram

import dto.telegram.`type`.ChatBoostSource

case class ChatBoostRemoved(
    chat: Chat,
    boostId: String,
    removeDate: Long,
    source: ChatBoostSource
)
