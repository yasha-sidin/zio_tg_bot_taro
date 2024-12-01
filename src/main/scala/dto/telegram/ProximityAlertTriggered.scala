package ru.otus
package dto.telegram

case class ProximityAlertTriggered(traveler: User, watcher: User, distance: Long)
