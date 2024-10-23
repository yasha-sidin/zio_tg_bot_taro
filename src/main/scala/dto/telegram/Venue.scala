package ru.otus
package dto.telegram

import zio.json._

@jsonMemberNames(SnakeCase)
@jsonDerive
case class Venue(
    location: Location,
    title: String,
    address: String,
    foursquareId: Option[String],
    foursquareType: Option[String],
    googlePlaceId: Option[String],
    googlePlaceType: Option[String]
)
