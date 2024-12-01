package ru.otus
package dto.telegram

case class LinkPreviewOptions(
    isDisabled: Option[Boolean],
    url: Option[String],
    preferSmallMedia: Option[Boolean],
    preferLargeMedia: Option[Boolean],
    showAboveText: Option[Boolean]
)
