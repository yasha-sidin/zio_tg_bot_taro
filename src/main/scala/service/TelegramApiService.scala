package ru.otus
package service

import client.TelegramClient
import dto.telegram.{BotCommand, Update}
import dto.telegram.request.{DeleteMyCommandsRequest, GetMyCommandsRequest, GetUpdatesRequest, SendMessageRequest, SetMyCommandsRequest}

import zio.http.Client
import zio.{&, RIO, Scope, ULayer, ZIO, ZLayer}
import zio.macros.accessible

@accessible
object TelegramApiService {

  type TelegramApiService = Service
  private type TelegramServiceEnv = TelegramClient.Service with Client & Scope

  trait Service {
    def resetUpdates(): RIO[TelegramServiceEnv, Unit]

    def setCommands(commands: List[BotCommand]): RIO[TelegramServiceEnv, Unit]

    def deleteCommands(): RIO[TelegramServiceEnv, Unit]

    def pollUpdates(offset: Option[Long]): RIO[TelegramServiceEnv, Option[List[Update]]]

    def sendMessage(messageRequest: SendMessageRequest): RIO[TelegramServiceEnv, Unit]
  }

  private class ServiceImpl extends Service {
    override def pollUpdates(offset: Option[Long]): RIO[TelegramServiceEnv, Option[List[Update]]] =
      for {
        updateResponse <- TelegramClient.getUpdates(GetUpdatesRequest(offset = offset))
      } yield updateResponse.map(_.result)

    override def resetUpdates(): RIO[TelegramServiceEnv, Unit] =
      TelegramClient.getUpdates(GetUpdatesRequest()).unit

    override def setCommands(commands: List[BotCommand]): RIO[TelegramServiceEnv, Unit] =
      for {
        fetchedCommands <- TelegramClient.getMyCommands(GetMyCommandsRequest(languageCode = Some("ru")))
        _ <- fetchedCommands match {
          case Some(value) => if (value.result == commands) ZIO.succeed() else TelegramClient.setMyCommands(SetMyCommandsRequest(commands, Some("ru")))
          case None => TelegramClient.setMyCommands(SetMyCommandsRequest(commands))
        }
      } yield ()

    override def deleteCommands(): RIO[TelegramServiceEnv, Unit] =
      TelegramClient.deleteMyCommands(DeleteMyCommandsRequest(languageCode = Some("ru"))).unit

    override def sendMessage(messageRequest: SendMessageRequest): RIO[TelegramServiceEnv, Unit] =
      TelegramClient.sendMessage(messageRequest).unit
  }

  val live: ULayer[TelegramApiService] = ZLayer.succeed(new ServiceImpl)
}
