package ru.otus
package dto.telegram

import zio.json._

@jsonMemberNames(SnakeCase)
@jsonDerive
case class Game(
    title: String,
    description: String,
    photo: Seq[PhotoSize],
    text: Option[String],
    textEntities: Option[Seq[MessageEntity]],
    animation: Option[Animation]
)
