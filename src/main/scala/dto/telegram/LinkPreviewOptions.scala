package ru.otus
package dto.telegram

case class LinkPreviewOptions(
    isDisabled: Option[Boolean] = None,
    url: Option[String] = None,
    preferSmallMedia: Option[Boolean] = None,
    preferLargeMedia: Option[Boolean] = None,
    showAboveText: Option[Boolean] = None
)
