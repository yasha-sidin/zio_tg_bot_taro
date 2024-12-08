package ru.otus
package client

import dto.telegram.request.{DeleteMyCommandsRequest, GetMyCommandsRequest, GetUpdatesRequest, SendMessageRequest, SetMyCommandsRequest}
import dto.telegram.{BotCommand, Message, TelegramResponse, Update}
import configuration.{Configuration, TelegramConfig}
import util.ClientUtil.requestPost

import zio._
import io.circe.generic.extras.auto._
import configuration.CirceConfig._

import zio.http.Client
import zio.macros.accessible

@accessible
object TelegramClient {

  type TelegramClient = Service

  trait Service {
    def getMyCommands(getMyCommandsRequest: GetMyCommandsRequest): RIO[Client & Scope, Option[TelegramResponse[List[BotCommand]]]]

    def deleteMyCommands(deleteMyCommandsRequest: DeleteMyCommandsRequest): RIO[Client & Scope, Option[TelegramResponse[Boolean]]]

    def setMyCommands(setMyCommandsRequest: SetMyCommandsRequest): RIO[Client & Scope, Option[TelegramResponse[Boolean]]]

    def getUpdates(getUpdatesRequest: GetUpdatesRequest): RIO[Client & Scope, Option[TelegramResponse[List[Update]]]]

    def sendMessage(sendMessageRequest: SendMessageRequest): RIO[Client & Scope, Option[TelegramResponse[Message]]]
  }

  private case class ServiceImpl(config: TelegramConfig) extends Service {
    private val mainUrl = s"${config.apiUrl}/bot${config.botToken}"

    override def getUpdates(getUpdatesRequest: GetUpdatesRequest): RIO[Client & Scope, Option[TelegramResponse[List[Update]]]] =
      requestPost[GetUpdatesRequest, TelegramResponse[List[Update]]](s"$mainUrl/getUpdates", getUpdatesRequest, logResponse = false, logPostBody = false)

    override def sendMessage(sendMessageRequest: SendMessageRequest): RIO[Client & Scope, Option[TelegramResponse[Message]]] =
      requestPost[SendMessageRequest, TelegramResponse[Message]](s"$mainUrl/sendMessage", sendMessageRequest, logResponse = true, logPostBody = false)

    override def getMyCommands(getMyCommandsRequest: GetMyCommandsRequest): RIO[Client & Scope, Option[TelegramResponse[List[BotCommand]]]] =
      requestPost[GetMyCommandsRequest, TelegramResponse[List[BotCommand]]](s"$mainUrl/getMyCommands", getMyCommandsRequest, logResponse = true, logPostBody = false)

    override def setMyCommands(setMyCommandsRequest: SetMyCommandsRequest): RIO[Client & Scope, Option[TelegramResponse[Boolean]]] =
      requestPost[SetMyCommandsRequest, TelegramResponse[Boolean]](s"$mainUrl/setMyCommands", setMyCommandsRequest, logResponse = true, logPostBody = false)

    override def deleteMyCommands(deleteMyCommandsRequest: DeleteMyCommandsRequest): RIO[Client & Scope, Option[TelegramResponse[Boolean]]] =
      requestPost[DeleteMyCommandsRequest, TelegramResponse[Boolean]](s"$mainUrl/deleteMyCommands", deleteMyCommandsRequest, logResponse = true, logPostBody = false)
  }

  val live: ZLayer[Configuration, Nothing, TelegramClient.Service] = ZLayer {
    for {
      config <- ZIO.service[Configuration]
    } yield ServiceImpl(config.telegram)
  }
}
