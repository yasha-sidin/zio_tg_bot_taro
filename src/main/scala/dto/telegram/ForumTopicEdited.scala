package ru.otus
package dto.telegram

import zio.json._

@jsonMemberNames(SnakeCase)
@jsonDerive
case class ForumTopicEdited(name: Option[String], iconCustomEmojiId: Option[String])
