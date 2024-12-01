package ru.otus
package dto.telegram

case class Venue(
    location: Location,
    title: String,
    address: String,
    foursquareId: Option[String],
    foursquareType: Option[String],
    googlePlaceId: Option[String],
    googlePlaceType: Option[String]
)
