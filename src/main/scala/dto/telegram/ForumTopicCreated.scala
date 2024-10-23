package ru.otus
package dto.telegram

import zio.json._

@jsonMemberNames(SnakeCase)
@jsonDerive
case class ForumTopicCreated(name: String, iconColor: Int, iconCustomEmojiId: Option[String])
