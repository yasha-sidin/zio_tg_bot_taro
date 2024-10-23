package ru.otus
package dto.telegram

import zio.json._

@jsonMemberNames(SnakeCase)
@jsonDerive
case class LinkPreviewOptions(
    isDisabled: Option[Boolean],
    url: Option[String],
    preferSmallMedia: Option[Boolean],
    preferLargeMedia: Option[Boolean],
    showAboveText: Option[Boolean]
)
