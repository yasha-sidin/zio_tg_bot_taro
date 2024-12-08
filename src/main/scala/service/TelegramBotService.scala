package ru.otus
package service

import client.TelegramClient
import repository.TelegramOffsetRepository
import `type`.MainCommand
import dao.{TelegramOffset, TelegramOffsetId}
import dto.telegram.{Message, ReplyKeyboardMarkup, Update}
import dto.telegram.request.SendMessageRequest

import zio.{&, RIO, Scope, ULayer, ZIO, ZLayer}
import zio.http.Client
import zio.macros.accessible

import javax.sql.DataSource

@accessible
object TelegramBotService {

  type TelegramBotService = Service
  private type TelegramBotServiceEnv = TelegramClient.Service with Client & Scope with TelegramApiService.Service with TelegramOffsetRepository.Service with DataSource

  trait Service {
    def process(time: Long): RIO[TelegramBotServiceEnv, Unit]
  }

  private class ServiceImpl extends Service {
    private val keyboard = ReplyKeyboardMarkup(
      keyboard = MainCommand.values.map(_.keyboardCommand).sortBy(_._2).groupBy(_._2).map(group => group._2.map(_._1)).toSeq,
      resizeKeyboard = Some(true),
      oneTimeKeyboard = Some(false)
    )

    private def processUpdate(update: Update, appTime: Long): ZIO[TelegramBotServiceEnv, Any, Unit] =
      for {
        _ <- update.message match {
          case Some(message) => processMessage(message).when(appTime <= message.date)
          case _ => ZIO.logInfo(s"Empty message from Update(id: ${update.updateId})")
        }
        _ <- TelegramOffsetRepository.updateOffset(TelegramOffset(0, update.updateId))
      } yield ()

    private def processMessage(message: Message): RIO[TelegramBotServiceEnv, Unit] = for {
      _ <- message.text match {
        case Some(text) => handleText(text, message.chat.id)
        case None => ZIO.succeed()
      }
    } yield ()

    private def handleText(text: String, chatId: Long): RIO[TelegramBotServiceEnv, Unit] = for {
      _ <- if (text == MainCommand.Info.keyboardCommand._1.text) {
        TelegramApiService.sendMessage(SendMessageRequest(text = "Info command", replyMarkup = Some(keyboard), chatId = chatId))
      } else if (text == MainCommand.Enroll.keyboardCommand._1.text) {
        TelegramApiService.sendMessage(SendMessageRequest(text = "Enroll command", replyMarkup = Some(keyboard), chatId = chatId))
      } else if (text == MainCommand.Contacts.keyboardCommand._1.text) {
        TelegramApiService.sendMessage(SendMessageRequest(text = "Contacts command", replyMarkup = Some(keyboard), chatId = chatId))
      } else {
        TelegramApiService.sendMessage(SendMessageRequest(text = "Not a command", replyMarkup = Some(keyboard), chatId = chatId))
      }
    } yield ()

    def process(time: Long): RIO[TelegramBotServiceEnv, Unit] =
      for {
        offset <- TelegramOffsetRepository.getOffsetById(TelegramOffsetId(0)).some.orElseFail(new Throwable("Empty offset in db"))
        updates <- TelegramApiService.pollUpdates(Some(offset.offsetValue + 1)).mapError(er => new Throwable(er))
        _ <- (updates match {
          case Some(value) => ZIO.foreachDiscard(value)(update => processUpdate(update, time))
          case None => ZIO.logWarning("None result of polling updates")
        }).orElseFail(new Throwable("Error in process"))
      } yield ()
  }

  val live: ULayer[TelegramBotService] = ZLayer.succeed(new ServiceImpl)
}
