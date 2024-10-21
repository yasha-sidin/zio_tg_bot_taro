package ru.otus
package dto.telegram

object ReplyParameters {}

case class ReplyParameters(
    messageId: String,
    chatId: Option[Long],
    allowSendingWithoutReply: Option[Boolean],
    quote: Option[String],
    quoteParseMode: Option[String],
    quoteEntities: Option[List[MessageEntity]],
    quotePosition: Option[Long]
)
