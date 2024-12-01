package ru.otus
package dto.telegram

case class User(
    id: Long,
    isBot: Boolean,
    firstName: String,
    lastName: Option[String],
    username: Option[String],
    languageCode: Option[String],
    isPremium: Option[Boolean],
    addedToAttachmentMenu: Option[Boolean],
    canJoinGroups: Option[Boolean],
    canReadAllGroupMessages: Option[Boolean],
    supportsInlineQueries: Option[Boolean]
)
