package ru.otus
package dto.telegram

import ru.otus.dto.telegram.`type`.ReactionType
import zio.json._

@jsonMemberNames(SnakeCase)
@jsonDerive
case class ReactionCount(
    `type`: ReactionType,
    totalCount: Long
)
