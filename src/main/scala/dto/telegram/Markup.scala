package ru.otus
package dto.telegram

object Markup {
//  implicit val markupJsonCodec: JsonValueCodec[Markup] = new JsonValueCodec[Markup] {
//
//    override def decodeValue(in: JsonReader, default: Markup): Markup = {
//      in.setMark()
//      val data = new String(in.readRawValAsBytes())
//      in.rollbackToMark()
//
//      if (data.contains("\"keyboard\"")) replyKeyboardMarkupJsonCodec.decodeValue(in, null)
//      else if (data.contains("\"force_reply\"")) forceReplyJsonCodec.decodeValue(in, null)
//      else if (data.contains("\"remove_keyboard\"")) replyKeyboardRemoveJsonCodec.decodeValue(in, null)
//      else inlineKeyboardMarkupJsonCodec.decodeValue(in, null)
//    }
//
//    override def encodeValue(markup: Markup, out: JsonWriter): Unit = markup match {
//      case markup: InlineKeyboardMarkup => inlineKeyboardMarkupJsonCodec.encodeValue(markup, out)
//      case markup: ReplyKeyboardMarkup  => replyKeyboardMarkupJsonCodec.encodeValue(markup, out)
//      case markup: ReplyKeyboardRemove  => replyKeyboardRemoveJsonCodec.encodeValue(markup, out)
//      case markup: ForceReply           => forceReplyJsonCodec.encodeValue(markup, out)
//    }
//
//    override def nullValue: Markup = null
//  }
}

trait Markup
