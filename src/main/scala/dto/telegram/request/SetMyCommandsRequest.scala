package ru.otus
package dto.telegram.request

import dto.telegram.BotCommand

case class SetMyCommandsRequest(commands: List[BotCommand], languageCode: Option[String] = None)
