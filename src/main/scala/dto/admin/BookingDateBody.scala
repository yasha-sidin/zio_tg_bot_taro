package ru.otus
package dto.admin

import dto.admin.`type`.BookingStatus

import java.time.Instant
import java.util.UUID

case class BookingDateBody(
    id: UUID,
    userId: UUID,
    bookNumber: Long,
    status: BookingStatus,
    canReturn: Boolean,
    timeToConfirm: Instant,
    createdAt: Instant,
    updatedAt: Instant,
    date: AppointmentDate,
  )
