package ru.otus
package service

import client.TelegramClient
import repository.{ChatStateRepository, TelegramOffsetRepository}
import `type`.{ChatStateType, MainCommand}
import dao.{ChatState, TelegramOffset, TelegramOffsetId}
import dto.telegram.{Markup, Message, Update}
import dto.telegram.request.SendMessageRequest
import util.KeyboardUtil

import zio.{&, RIO, Scope, ULayer, ZIO, ZLayer}
import zio.http.Client
import zio.macros.accessible

import java.util.UUID
import javax.sql.DataSource

@accessible
object TelegramBotService {

  type TelegramBotService = Service
  private type TelegramBotServiceEnv =
    TelegramClient.Service with Client & Scope with TelegramApiService.Service with TelegramOffsetRepository.Service with ChatStateRepository.Service with ChatStateService.Service with DataSource

  trait Service {
    def process(time: Long): RIO[TelegramBotServiceEnv, Unit]
  }

  private class ServiceImpl extends Service {

    def process(time: Long): RIO[TelegramBotServiceEnv, Unit] =
      for {
        offset <- TelegramOffsetRepository.getOffsetById(TelegramOffsetId(0)).some.orElseFail(new Throwable("Empty offset in db"))
        updates <- TelegramApiService.pollUpdates(Some(offset.offsetValue + 1)).mapError(er => new Throwable(er))
        _ <- updates match {
          case Some(value) => ZIO.foreachDiscard(value)(update => Handler.handleUpdate(update, time))
          case None => ZIO.logWarning("None result of polling updates")
        }
      } yield ()
  }

  private object Handler {
    def handleUpdate(update: Update, appTime: Long): RIO[TelegramBotServiceEnv, Unit] = for {
      keyboard <- KeyboardUtil.getMainKeyboard
      _ <- update.message match {
        case Some(message) => handleMessage(message, keyboard).when(appTime <= message.date)
        case _ => ZIO.logInfo(s"Empty message from Update(id: ${update.updateId})")
      }
      _ <- TelegramOffsetRepository.updateOffset(TelegramOffset(0, update.updateId)).mapError(er => new Throwable(s"Db error: $er"))
    } yield ()

    private def handleMessage(message: Message, keyboard: Markup): RIO[TelegramBotServiceEnv, Unit] = for {
      state <- ChatStateService.getOrCreateStateByChatId(message.chat.id)
      _ <- message.text match {
        case Some(text) => state.chatStateType match {
          case ChatStateType.MainMenu => handleMainMenu(message.chat.id, text, state, keyboard)
//          case ChatStateType.MonthSelection => handleMonthSelection(message.chat.id, text, keyboard)
//          case ChatStateType.DaySelection => handleDaySelection(message.chat.id, text, keyboard)
//          case ChatStateType.TimeSelection => handleTimeSelection(message.chat.id, text, keyboard)
//          case ChatStateType.Confirmation => handleConfirmation(message.chat.id, text, keyboard)
          case ChatStateType.MonthSelection => handleMainMenu(message.chat.id, text, state, keyboard)
          case ChatStateType.DaySelection => handleMainMenu(message.chat.id, text, state, keyboard)
          case ChatStateType.TimeSelection => handleMainMenu(message.chat.id, text, state, keyboard)
          case ChatStateType.Confirmation => handleMainMenu(message.chat.id, text, state, keyboard)
        }
        case None => wrongFormat(keyboard, message.chat.id)
      }
    } yield ()

    private def handleMainMenu(chatId: Long, text: String, state: ChatState, keyboard: Markup): RIO[TelegramBotServiceEnv, Unit] = for {
      info <- ZIO.succeed(MainCommand.Info.keyboardCommand._1.text)
      enroll <- ZIO.succeed(MainCommand.Enroll.keyboardCommand._1.text)
      contacts <- ZIO.succeed(MainCommand.Contacts.keyboardCommand._1.text)
      _ <- text match {
        case `info` => TelegramApiService.sendMessage(SendMessageRequest(text = "Info command", replyMarkup = Some(keyboard), chatId = chatId))
        case `enroll` => for {
          monthKeyboard <- KeyboardUtil.getMonthKeyboard
          _ <- ChatStateService.updateState(state.copy(chatStateType = ChatStateType.MonthSelection))
          _ <- TelegramApiService.sendMessage(SendMessageRequest(text = "Выберите месяц для записи", replyMarkup = Some(monthKeyboard), chatId = chatId))
        } yield ()
        case `contacts` => TelegramApiService.sendMessage(SendMessageRequest(text = "Contacts command", replyMarkup = Some(keyboard), chatId = chatId))
        case _ => wrongCommand(keyboard, chatId)
      }
    } yield ()

    private def wrongCommand(keyboard: Markup, chatId: Long): RIO[TelegramBotServiceEnv, Unit] =
      TelegramApiService.sendMessage(SendMessageRequest(text = "Команда неверная", replyMarkup = Some(keyboard), chatId = chatId))

    private def wrongFormat(keyboard: Markup, chatId: Long): RIO[TelegramBotServiceEnv, Unit] =
      TelegramApiService.sendMessage(SendMessageRequest(text = "Формат неверный", replyMarkup = Some(keyboard), chatId = chatId))
  }

  val live: ULayer[TelegramBotService] = ZLayer.succeed(new ServiceImpl)
}
