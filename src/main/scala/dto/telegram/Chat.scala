package ru.otus
package dto.telegram

import dto.telegram.`type`.ChatType

case class Chat(
    id: Long,
    `type`: ChatType,
    title: Option[String],
    username: Option[String],
    firstName: Option[String],
    lastName: Option[String],
    isForum: Option[Boolean],
    photo: Option[ChatPhoto],
    activeUsernames: Option[Seq[String]],
    emojiStatusCustomEmojiId: Option[String],
    bio: Option[String],
    hasPrivateForwards: Option[Boolean],
    hasRestrictedVoiceAndVideoMessages: Option[Boolean],
    joinToSendMessages: Option[Boolean],
    joinByRequest: Option[Boolean],
    description: Option[String],
    inviteLink: Option[String],
    pinnedMessage: Option[Message],
    permissions: Option[ChatPermissions],
    slowModeDelay: Option[Long],
    messageAutoDeleteTime: Option[Long],
    hasAggressiveAntiSpamEnabled: Option[Boolean],
    hasHiddenMembers: Option[Boolean],
    hasProtectedContent: Option[Boolean],
    stickerSetName: Option[String],
    canSetStickerSet: Option[Boolean],
    linkedChatId: Option[Long],
    location: Option[ChatLocation]
)
