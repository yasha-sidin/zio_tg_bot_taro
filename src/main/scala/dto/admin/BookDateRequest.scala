package ru.otus
package dto.admin

import java.util.UUID

case class BookDateRequest(
    dateId: UUID,
    telegramDetails: TelegramDetails,
  )
