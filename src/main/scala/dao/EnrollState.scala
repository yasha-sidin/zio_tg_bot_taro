package ru.otus
package dao

import `type`.EnrollCommandState

case class EnrollState(id: String, chatId: Long, enrollCommandState: EnrollCommandState)

case class EnrollStateId(id: String)

case class ChatId(id: Long)