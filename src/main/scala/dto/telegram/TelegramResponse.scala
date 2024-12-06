package ru.otus
package dto.telegram

case class TelegramResponse[T](ok: Boolean, result: T)