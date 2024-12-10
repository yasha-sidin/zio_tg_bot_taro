package ru.otus
package dao

import `type`.ChatStateType

import java.util.UUID

case class ChatState(
                      id: UUID,
                      chatId: Long,
                      chatStateType: ChatStateType,
                      selectedMonth: Option[String] = None,
                      selectedDay: Option[Int] = None,
                      selectedTime: Option[String] = None
                    )

case class ChatStateId(id: UUID)

case class ChatId(id: Long)