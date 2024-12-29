package ru.otus
package dao

import `type`.ChatStateType

import java.util.UUID

case class ChatState(
    id: UUID,
    chatId: Long,
    chatStateType: ChatStateType,
    selectedAppointmentToCancel: Option[UUID] = None,
    selectedMonth: Option[String] = None,
    selectedDay: Option[String] = None,
    selectedTime: Option[String] = None
)

case class ChatStateId(id: UUID)

case class ChatId(id: Long)
