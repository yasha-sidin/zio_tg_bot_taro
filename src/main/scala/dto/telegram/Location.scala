package ru.otus
package dto.telegram

case class Location(
    longitude: Double,
    latitude: Double,
    horizontalAccuracy: Option[Double],
    livePeriod: Option[Int],
    heading: Option[Int],
    proximityAlertRadius: Option[Int]
)
