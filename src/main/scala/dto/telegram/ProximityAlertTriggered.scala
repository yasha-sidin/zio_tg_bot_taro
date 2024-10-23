package ru.otus
package dto.telegram

import zio.json._

@jsonMemberNames(SnakeCase)
@jsonDerive
case class ProximityAlertTriggered(traveler: User, watcher: User, distance: Long)
