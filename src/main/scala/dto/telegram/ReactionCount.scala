package ru.otus
package dto.telegram

import dto.telegram.`type`.ReactionType

case class ReactionCount(
    `type`: ReactionType,
    totalCount: Long
)
