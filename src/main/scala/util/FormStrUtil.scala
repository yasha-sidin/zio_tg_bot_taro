package ru.otus
package util

import dto.admin.BookingDateBody

import zio.{ UIO, ZIO }

object FormStrUtil {
  def formStrAllBookings(bookings: List[BookingDateBody]): UIO[String] =
    ZIO.succeed(
      bookings match {
        case Nil => "У вас пока нет записей"
        case _   => "История ваших записей:\n" + bookings.map(b => s"${TimeUtil.toFormatWithTimeZone(b.date)._2} - ${b.status.translation}").mkString("\n")
      }
    )

  def formCancelBookings(bookings: List[BookingDateBody]): UIO[String] =
    ZIO.succeed(
      bookings match {
        case Nil => "У вас пока нет записей"
        case _   => "Выберите запись для отмены:\n" + bookings.map(b => s"${b.bookNumber}.: ${TimeUtil.toFormatWithTimeZone(b.date)._2} - ${b.status.translation}").mkString("\n")
      }
    )
}
