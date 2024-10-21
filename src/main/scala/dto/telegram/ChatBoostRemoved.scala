package ru.otus
package dto.telegram

object ChatBoostRemoved {}

case class ChatBoostRemoved(
    chat: Chat,
    boostId: String,
    removeDate: Long,
    source: ChatBoostSource
)
