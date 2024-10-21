package ru.otus
package dto.telegram

object ChatBoostUpdated {}

case class ChatBoostUpdated(
    chat: Chat,
    boost: ChatBoost
)
