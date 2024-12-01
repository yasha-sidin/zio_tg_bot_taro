package ru.otus
package dto.telegram

import dto.telegram.`type`.StickerType

case class Sticker(
    fileId: String,
    fileUniqueId: String,
    `type`: StickerType,
    width: Int,
    height: Int,
    isAnimated: Boolean,
    isVideo: Boolean,
    thumb: Option[PhotoSize],
    emoji: Option[String],
    setName: Option[String],
    premiumAnimation: Option[File],
    maskPosition: Option[MaskPosition],
    customEmojiId: Option[String],
    fileSize: Option[Long]
)
