package ru.otus
package service

import client.TelegramClient
import dao.{TelegramOffset, TelegramOffsetId}
import dto.telegram.{BotCommand, Update}
import dto.telegram.request.{GetMyCommandsRequest, GetUpdatesRequest, SendMessageRequest, SetMyCommandsRequest}
import repository.TelegramOffsetRepository

import zio.http.Client
import zio.{&, RIO, Scope, ULayer, ZIO, ZLayer}
import zio.macros.accessible

import javax.sql.DataSource

@accessible
object TelegramService {

  type TelegramService = Service
  private type TelegramServiceEnv = TelegramClient.Service with Client & Scope with TelegramOffsetRepository.Service with DataSource

  trait Service {
    def resetUpdates(): RIO[TelegramServiceEnv, Unit]

    def process(time: Long): RIO[TelegramServiceEnv, Unit]

    def checkCommands(commands: List[BotCommand]): RIO[TelegramServiceEnv, Unit]
  }

  class ServiceImpl extends Service {

    private def pollUpdates(offset: Option[Long]): RIO[TelegramServiceEnv, Option[List[Update]]] =
      for {
        updateResponse <- TelegramClient.getUpdates(GetUpdatesRequest(offset = offset))
      } yield updateResponse.map(_.result)

    private def processUpdate(update: Update): ZIO[TelegramServiceEnv, Any, Unit] =
      for {
        chatId <- ZIO.succeed(update.message.map(_.chat.id))
        _ <- ZIO.succeed(chatId).flatMap {
          case Some(id) =>
            TelegramClient.sendMessage(SendMessageRequest(chatId = id, text = "Hello, Telegram!!!"))
          case None => ZIO.logInfo(s"Empty message from Update(id: ${update.updateId})")
        }
        _ <- TelegramOffsetRepository.updateOffset(TelegramOffset(0, update.updateId))
      } yield ()

    def resetUpdates(): RIO[TelegramServiceEnv, Unit] =
      for {
        _ <- TelegramClient.getUpdates(GetUpdatesRequest())
      } yield ()

    def process(time: Long): RIO[TelegramServiceEnv, Unit] =
      for {
        offset <- TelegramOffsetRepository.getOffsetById(TelegramOffsetId(0)).some.orElseFail(new Throwable("empty offset in db"))
        updates <- pollUpdates(Some(offset.offsetValue + 1)).mapError(er => new Throwable(er))
        _ <- (updates match {
          case Some(value) => ZIO.foreachParDiscard(value)(update => update.message.map(_.date) match {
            case Some(messageTime) => processUpdate(update).when(time <= messageTime)
            case None => ZIO.succeed(false)
          })
          case None => ZIO.succeed()
        }).orElseFail(new Throwable("process error"))
      } yield ()

    override def checkCommands(commands: List[BotCommand]): RIO[TelegramServiceEnv, Unit] =
      for {
        fetchedCommands <- TelegramClient.getMyCommands(GetMyCommandsRequest())
        _ <- fetchedCommands match {
          case Some(value) => if (value.result == commands) ZIO.succeed() else TelegramClient.setMyCommands(SetMyCommandsRequest(commands, Some("ru")))
          case None => TelegramClient.setMyCommands(SetMyCommandsRequest(commands))
        }
      } yield ()
  }

  val live: ULayer[ServiceImpl] = ZLayer.succeed(new ServiceImpl)
}
