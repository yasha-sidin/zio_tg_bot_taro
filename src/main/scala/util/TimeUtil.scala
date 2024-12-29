package ru.otus
package util

import dto.admin.AppointmentDate
import java.time.Duration

import java.time.format.DateTimeFormatter
import java.time.{LocalDateTime, ZoneId}
import java.time.format.TextStyle
import java.util.Locale
import scala.collection.MapView

object TimeUtil {
  private val zoneId                       = ZoneId.of("Europe/Moscow")
  private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")

  def secondsToTime(seconds: Long): String = {
    val duration = Duration.ofSeconds(seconds)
    val hours = duration.toHours
    val minutes = duration.toMinutes % 60
    val secs = duration.getSeconds % 60
    f"$hours%02d:$minutes%02d:$secs%02d"
  }

  private val russianMonths = List(
    "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь",
    "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"
  )

  def toFormatWithTimeZone(date: AppointmentDate): (AppointmentDate, String) =
    (date, date.dateFrom.atZone(zoneId).format(formatter))

  private def toLocalDateTime(tuple: (AppointmentDate, String)): (AppointmentDate, LocalDateTime) = (tuple._1, LocalDateTime.parse(tuple._2, formatter))

  def groupedDates(list: List[AppointmentDate]): MapView[String, MapView[String, List[(AppointmentDate, String)]]] =
    list
      .map(toFormatWithTimeZone)
      .map(toLocalDateTime)
      .groupBy(tuple => {
        val monthIndex = tuple._2.getMonthValue - 1
        russianMonths(monthIndex)
      })
      .view.mapValues(
        _.groupBy(tuple => s"${tuple._2.getDayOfMonth}.${tuple._2.getMonthValue}.${tuple._2.getYear}")
          .view.mapValues(dates => dates.map(tuple => (tuple._1, tuple._2.toLocalTime.toString)))
      )
}
