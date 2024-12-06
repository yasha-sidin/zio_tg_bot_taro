package ru.otus
package dto.telegram.request

import dto.telegram.{LinkPreviewOptions, Markup, MessageEntity, ReplyParameters}

case class SendMessageRequest(
    businessConnectionId: Option[String] = None,
    chatId: Long,
    messageThreadId: Option[Long] = None,
    text: String,
    parseMode: Option[String] = None,
    entities: Option[List[MessageEntity]] = None,
    linkPreviewOptions: Option[LinkPreviewOptions] = None,
    disableNotification: Option[Boolean] = None,
    protectContent: Option[Boolean] = None,
    messageEffectId: Option[String] = None,
    replyParameters: Option[ReplyParameters] = None,
    replyMarkup: Option[Markup] = None
)
