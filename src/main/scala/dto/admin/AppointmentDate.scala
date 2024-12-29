package ru.otus
package dto.admin

import dto.admin.`type`.AppointmentDateStatus

import java.time.Instant
import java.util.UUID

case class AppointmentDate(
    id: UUID,
    dateFrom: Instant,
    dateTo: Instant,
    status: AppointmentDateStatus,
    bookingDeadline: Instant,
    createdAt: Instant,
    updatedAt: Instant,
  )
