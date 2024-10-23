package ru.otus
package dto.telegram.request

import dto.telegram.{LinkPreviewOptions, Markup, MessageEntity, ReplyParameters}

object SendMessageRequest {}

import zio.json._

@jsonMemberNames(SnakeCase)
@jsonDerive
case class SendMessageRequest(
    businessConnectionId: String,
    chatId: Long,
    messageThreadId: Long,
    text: String,
    parseMode: Option[String],
    entities: Option[List[MessageEntity]],
    linkPreviewOptions: Option[LinkPreviewOptions],
    disableNotification: Option[Boolean],
    protectContent: Option[Boolean],
    messageEffectId: Option[String],
    replyParameters: Option[ReplyParameters],
    replyMarkup: Option[Markup]
)
