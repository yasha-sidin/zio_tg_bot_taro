package ru.otus
package service

import client.{ BackendClient, TelegramClient }
import repository.{ ChatStateRepository, TelegramOffsetRepository }
import `type`.{ ChatStateType, MainCommand }
import dao.{ ChatState, TelegramOffset, TelegramOffsetId }
import dto.telegram.{ Markup, Message, Update }
import dto.telegram.request.SendMessageRequest
import util.{ FormStrUtil, KeyboardUtil, TimeUtil }
import dto.admin.`type`.AppConstant
import dto.admin.{ BookDateRequest, Constant, TelegramDetails }

import zio.{ &, RIO, Scope, ULayer, ZIO, ZLayer }
import zio.http.Client
import zio.macros.accessible

import java.time.Instant
import javax.sql.DataSource

@accessible
object TelegramBotService {

  type TelegramBotService            = Service
  private type TelegramBotServiceEnv =
    TelegramClient.Service with Client &
      Scope with TelegramApiService.Service with TelegramOffsetRepository.Service with ChatStateRepository.Service with ChatStateService.Service with DataSource with BackendClient.Service with BackendService.Service

  trait Service {
    def process(time: Long): RIO[TelegramBotServiceEnv, Unit]
  }

  private class ServiceImpl extends Service {

    def process(time: Long): RIO[TelegramBotServiceEnv, Unit] =
      for {
        offset  <- TelegramOffsetRepository.getOffsetById(TelegramOffsetId(0)).some.orElseFail(new Throwable("Empty offset in db"))
        updates <- TelegramApiService.pollUpdates(Some(offset.offsetValue + 1)).mapError(er => new Throwable(er))
        _       <- updates match {
                     case Some(value) => ZIO.foreachDiscard(value)(update => Handler.handleUpdate(update, time))
                     case None        => ZIO.logWarning("None result of polling updates")
                   }
      } yield ()
  }

  private object Handler {

    def handleUpdate(update: Update, appTime: Long): RIO[TelegramBotServiceEnv, Unit] = for {
      keyboard <- KeyboardUtil.getMainKeyboard
      _        <- (update.message match {
                    case Some(message) => handleMessage(message, keyboard).when(appTime <= message.date)
                    case _             => ZIO.logInfo(s"Empty message from Update(id: ${update.updateId})")
                  }).mapError(er => new Throwable(er.toString))
      _        <- TelegramOffsetRepository.updateOffset(TelegramOffset(0, update.updateId)).mapError(er => new Throwable(s"Db error: $er"))
    } yield ()

    private def handleMessage(message: Message, keyboard: Markup): ZIO[TelegramBotServiceEnv, Serializable, Unit] = for {
      state <- ChatStateService.getOrCreateStateByChatId(message.chat.id)
      _     <- message.text match {
                 case Some(text) =>
                   state.chatStateType match {
                     case ChatStateType.MainMenu                  => handleMainMenu(message.chat.id, text, state, keyboard)
                     case ChatStateType.BookingCancel             => handleBookingsCancel(message.chat.id, text, state, keyboard)
                     case ChatStateType.BookingCancelConfirmation => handleBookingCancelConfirmation(message.chat.id, text, state, keyboard)
                     case ChatStateType.MonthSelection            => handleMonthSelection(message.chat.id, text, state, keyboard)
                     case ChatStateType.DaySelection              => handleDaySelection(message.chat.id, text, state, keyboard)
                     case ChatStateType.TimeSelection             => handleTimeSelection(message.chat.id, text, state, keyboard)
                     case ChatStateType.Confirmation              => handleConfirmation(message, text, state, keyboard)
                   }
                 case None       => wrongFormat(keyboard, message.chat.id)
               }
    } yield ()

    private def handleMainMenu(
        chatId: Long,
        text: String,
        state: ChatState,
        keyboard: Markup,
      ): RIO[TelegramBotServiceEnv, Unit] = for {
      info          <- ZIO.succeed(MainCommand.Info.keyboardCommand._1.text)
      enroll        <- ZIO.succeed(MainCommand.Enroll.keyboardCommand._1.text)
      contacts      <- ZIO.succeed(MainCommand.Contacts.keyboardCommand._1.text)
      myBookings    <- ZIO.succeed(MainCommand.MyBookings.keyboardCommand._1.text)
      cancelBooking <- ZIO.succeed(MainCommand.CancelBooking.keyboardCommand._1.text)
      _             <- text match {
                         case `info`          =>
                           for {
                             info <- BackendService.getConstantByKey(AppConstant.AboutMe)
                             _    <- info match {
                                       case Right(value) => TelegramApiService.sendMessage(SendMessageRequest(text = value.value, replyMarkup = Some(keyboard), chatId = chatId))
                                       case Left(error)  => resetMenu(keyboard, chatId, error.message, state)
                                     }
                           } yield ()
                         case `enroll`        =>
                           for {
                             groupedMap <- BackendService.getAvailableDatesMap
                             _          <- groupedMap match {
                                             case Right(value) =>
                                               for {
                                                 str      <- ZIO.succeed("Выберите месяц для записи")
                                                 keyboard <- KeyboardUtil.getStringKeyboardWithCancel(value.toSeq.map(_._1))
                                                 _        <- ChatStateService.updateState(state.copy(chatStateType = ChatStateType.MonthSelection))
                                                 _        <- TelegramApiService.sendMessage(SendMessageRequest(text = str, replyMarkup = Some(keyboard), chatId = chatId))
                                               } yield ()
                                             case Left(error)  => resetMenu(keyboard, chatId, error.message, state)
                                           }
                           } yield ()
                         case `contacts`      =>
                           for {
                             contact <- BackendService.getConstantByKey(AppConstant.MyContacts)
                             _       <- contact match {
                                          case Right(value) => TelegramApiService.sendMessage(SendMessageRequest(text = value.value, replyMarkup = Some(keyboard), chatId = chatId))
                                          case Left(error)  => resetMenu(keyboard, chatId, error.message, state)
                                        }
                           } yield ()
                         case `myBookings`    =>
                           for {
                             bookings <- BackendService.getUserBookings(chatId)
                             _        <- bookings match {
                                           case Right(value) =>
                                             for {
                                               str <- FormStrUtil.formStrAllBookings(value)
                                               _   <- TelegramApiService.sendMessage(SendMessageRequest(text = str, replyMarkup = Some(keyboard), chatId = chatId))
                                             } yield ()
                                           case Left(error)  => resetMenu(keyboard, chatId, error.message, state)
                                         }
                           } yield ()
                         case `cancelBooking` =>
                           for {
                             bookings <- BackendService.getBookingsToCancel(chatId)
                             _        <- bookings match {
                                           case Right(value) =>
                                             for {
                                               str      <- FormStrUtil.formCancelBookings(value)
                                               keyboard <- if (value.nonEmpty) {
                                                             KeyboardUtil.getStringKeyboardWithCancel(value.map(_.bookNumber.toString))
                                                           }
                                                           else {
                                                             ZIO.succeed(keyboard)
                                                           }
                                               _        <- ChatStateService.updateState(state.copy(chatStateType = ChatStateType.BookingCancel))
                                               _        <- TelegramApiService.sendMessage(SendMessageRequest(text = str, replyMarkup = Some(keyboard), chatId = chatId))
                                             } yield ()
                                           case Left(error)  => resetMenu(keyboard, chatId, error.message, state)
                                         }
                           } yield ()
                         case _               => wrongCommand(keyboard, chatId)
                       }
    } yield ()

    private def handleBookingsCancel(
        chatId: Long,
        text: String,
        state: ChatState,
        keyboard: Markup,
      ): RIO[TelegramBotServiceEnv, Unit] = for {
      bookings <- BackendService.getBookingsToCancel(chatId)
      _        <- bookings match {
                    case Right(value) =>
                      for {
                        checkCommand <- ZIO.succeed(value.find(_.bookNumber.toString == text))
                        _            <- checkCommand match {
                                          case Some(value) =>
                                            for {
                                              _        <- ChatStateService.updateState(state.copy(chatStateType = ChatStateType.BookingCancelConfirmation, selectedAppointmentToCancel = Some(value.id)))
                                              now      <- ZIO.succeed(Instant.now())
                                              str      <- ZIO.succeed(if (value.date.bookingDeadline.isBefore(now)) {
                                                            s"Вы уверены, что хотите отменить запись на ${TimeUtil.toFormatWithTimeZone(value.date)._2}? Предоплата не возвращается."
                                                          }
                                                          else {
                                                            s"Вы уверены, что хотите отменить запись на ${TimeUtil.toFormatWithTimeZone(value.date)._2}? Предоплата возвращается."
                                                          })
                                              keyboard <- KeyboardUtil.getConfirmationKeyboard
                                              _        <- TelegramApiService.sendMessage(SendMessageRequest(text = str, replyMarkup = Some(keyboard), chatId = chatId))
                                            } yield ()
                                          case None        => resetMenu(keyboard, chatId, "Отмена", state)
                                        }
                      } yield ()
                    case Left(error)  => resetMenu(keyboard, chatId, error.message, state)
                  }
    } yield ()

    private def handleBookingCancelConfirmation(
        chatId: Long,
        text: String,
        state: ChatState,
        keyboard: Markup,
      ) =
      text match {
        case "Подтвердить" =>
          for {
            id       <- ZIO.succeed(state.selectedAppointmentToCancel).some.orElseFail("Empty selectedAppointmentToCancel")
            canceled <- BackendService.cancelBooking(id)
            _        <- canceled match {
                          case Right(value) => resetMenu(keyboard, chatId, s"Запись номер ${value.bookNumber} на ${TimeUtil.toFormatWithTimeZone(value.date)._2} была успешно отменена", state)
                          case Left(error)  => resetMenu(keyboard, chatId, error.message, state)
                        }
          } yield ()
        case "Отмена"      => resetMenu(keyboard, chatId, "Отмена", state)
      }

    private def handleMonthSelection(
        chatId: Long,
        text: String,
        state: ChatState,
        keyboard: Markup,
      ) = for {
      groupedMap <- BackendService.getAvailableDatesMap
      _          <- groupedMap match {
                      case Right(value) =>
                        for {
                          month <- ZIO.succeed(value.toSeq.map(_._1).find(_ == text))
                          _     <- month match {
                                     case Some(m) =>
                                       for {
                                         _        <- ChatStateService.updateState(state.copy(selectedMonth = Some(m), chatStateType = ChatStateType.DaySelection))
                                         str      <- ZIO.succeed("Выберите число для записи")
                                         keyboard <- KeyboardUtil.getStringKeyboardWithCancel(value.toSeq.filter(_._1 == m).flatMap(_._2.toSeq.map(_._1)))
                                         _        <- TelegramApiService.sendMessage(SendMessageRequest(text = str, replyMarkup = Some(keyboard), chatId = chatId))
                                       } yield ()
                                     case None    =>
                                       text match {
                                         case "Отмена" => resetMenu(keyboard, chatId, "Отмена", state)
                                         case _        => resetMenu(keyboard, chatId, "Месяц выбран неправильно, либо больше нет записи на этот месяц", state)
                                       }
                                   }
                        } yield ()
                      case Left(error)  => resetMenu(keyboard, chatId, error.message, state)
                    }
    } yield ()

    private def handleDaySelection(
        chatId: Long,
        text: String,
        state: ChatState,
        keyboard: Markup,
      ) = for {
      groupedMap <- BackendService.getAvailableDatesMap
      _          <- groupedMap match {
                      case Right(value) =>
                        for {
                          tuple <- ZIO.succeed(state.selectedMonth.flatMap(month => value.toSeq.filter(_._1 == month).flatMap(_._2.toSeq.map(_._1)).find(_ == text).zip(Some(month))))
                          _     <- tuple match {
                                     case Some((day, month)) =>
                                       for {
                                         _        <- ChatStateService.updateState(state.copy(selectedDay = Some(day), chatStateType = ChatStateType.TimeSelection))
                                         str      <- ZIO.succeed("Выберите время для записи")
                                         keyboard <- KeyboardUtil.getStringKeyboardWithCancel(value.toSeq.filter(_._1 == month).flatMap(_._2.toSeq.filter(_._1 == day).flatMap(_._2.map(_._2))))
                                         _        <- TelegramApiService.sendMessage(SendMessageRequest(text = str, replyMarkup = Some(keyboard), chatId = chatId))
                                       } yield ()
                                     case None               =>
                                       text match {
                                         case "Отмена" => resetMenu(keyboard, chatId, "Отмена", state)
                                         case _        => resetMenu(keyboard, chatId, "Число выбрано неправильно, либо больше нет записи на эту дату", state)
                                       }
                                   }
                        } yield ()
                      case Left(error)  => resetMenu(keyboard, chatId, error.message, state)
                    }
    } yield ()

    private def handleTimeSelection(
        chatId: Long,
        text: String,
        state: ChatState,
        keyboard: Markup,
      ) = for {
      groupedMap <- BackendService.getAvailableDatesMap
      _          <- groupedMap match {
                      case Right(value) =>
                        for {
                          tuple <- ZIO.succeed(
                                     state
                                       .selectedMonth
                                       .zip(state.selectedDay)
                                       .flatMap(tuple =>
                                         value
                                           .toSeq
                                           .filter(_._1 == tuple._1)
                                           .flatMap(_._2.toSeq.filter(_._1 == tuple._2).flatMap(_._2.map(_._2)))
                                           .find(_ == text)
                                           .zip(Some(tuple))
                                       )
                                   )
                          _     <- tuple match {
                                     case Some((time, (month, day))) =>
                                       for {
                                         _             <- ChatStateService.updateState(state.copy(selectedTime = Some(time), chatStateType = ChatStateType.Confirmation))
                                         date          <- ZIO.succeed(value.toSeq.filter(_._1 == month).flatMap(_._2.toSeq.filter(_._1 == day).flatMap(_._2)).find(_._2 == time).map(_._1)).some
                                         timeToConfirm <-
                                           BackendService.getConstantByKey(AppConstant.MaxTimeToConfirm).right.map(constant => Constant.toValue[Long](constant)(AppConstant.MaxTimeToConfirm.deserialize))
                                         str           <-
                                           ZIO.succeed(s"Вы подтверждаете запись на ${TimeUtil.toFormatWithTimeZone(date)._2}. Вам будет нужно внести предоплату в течение ${TimeUtil.secondsToTime(timeToConfirm)}")
                                         keyboard      <- KeyboardUtil.getConfirmationKeyboard
                                         _             <- TelegramApiService.sendMessage(SendMessageRequest(text = str, replyMarkup = Some(keyboard), chatId = chatId))
                                       } yield ()
                                     case None                       =>
                                       text match {
                                         case "Отмена" => resetMenu(keyboard, chatId, "Отмена", state)
                                         case _        => resetMenu(keyboard, chatId, "Время выбрано неправильно, либо больше нет записи на это время", state)
                                       }
                                   }
                        } yield ()
                      case Left(error)  => resetMenu(keyboard, chatId, error.message, state)
                    }
    } yield ()

    private def handleConfirmation(
        message: Message,
        text: String,
        state: ChatState,
        keyboard: Markup,
      ) = text match {
      case "Подтвердить" =>
        for {
          groupedMap <- BackendService.getAvailableDatesMap
          _          <- groupedMap match {
                          case Right(value) =>
                            for {
                              date <- ZIO.succeed(
                                        state
                                          .selectedMonth
                                          .zip(state.selectedDay)
                                          .zip(state.selectedTime)
                                          .flatMap(tuple =>
                                            value
                                              .toSeq
                                              .filter(_._1 == tuple._1._1)
                                              .flatMap(_._2.toSeq.filter(_._1 == tuple._1._2).flatMap(_._2))
                                              .find(_._2 == tuple._2)
                                              .map(_._1)
                                          )
                                      )
                              _    <- date match {
                                        case Some(value) =>
                                          for {
                                            _       <- ChatStateService.updateState(state.copy(chatStateType = ChatStateType.Confirmation))
                                            booking <- BackendService.book(
                                                         BookDateRequest(
                                                           value.id,
                                                           TelegramDetails(
                                                             message.chat.id,
                                                             message.from.map(_.firstName).getOrElse(""),
                                                             message.from.flatMap(_.lastName),
                                                             message.from.flatMap(_.username),
                                                             message.from.flatMap(_.languageCode),
                                                           ),
                                                         )
                                                       )
                                            _       <- booking match {
                                                         case Right(res)  => resetMenu(keyboard, message.chat.id, s"Вы успешно записались на ${TimeUtil.toFormatWithTimeZone(res.date)._2}", state)
                                                         case Left(error) => resetMenu(keyboard, message.chat.id, error.message, state)
                                                       }
                                          } yield ()
                                        case None        => resetMenu(keyboard, message.chat.id, "Время уже забронировали", state)
                                      }
                            } yield ()
                          case Left(error)  => resetMenu(keyboard, message.chat.id, error.message, state)
                        }
        } yield ()
      case "Отмена"      => resetMenu(keyboard, message.chat.id, "Отмена", state)
      case _             => wrongCommand(keyboard, message.chat.id)
    }

    private def wrongCommand(keyboard: Markup, chatId: Long): RIO[TelegramBotServiceEnv, Unit] =
      TelegramApiService.sendMessage(SendMessageRequest(text = "Команда неверная", replyMarkup = Some(keyboard), chatId = chatId))

    private def wrongFormat(keyboard: Markup, chatId: Long): RIO[TelegramBotServiceEnv, Unit] =
      TelegramApiService.sendMessage(SendMessageRequest(text = "Формат неверный", replyMarkup = Some(keyboard), chatId = chatId))

    private def resetMenu(
        keyboard: Markup,
        chatId: Long,
        text: String,
        state: ChatState,
      ): ZIO[TelegramBotServiceEnv, Throwable, Unit] =
      for {
        _ <- ChatStateService.updateState(
               state.copy(
                 chatStateType = ChatStateType.MainMenu,
                 selectedAppointmentToCancel = None,
                 selectedMonth = None,
                 selectedDay = None,
                 selectedTime = None,
               )
             )
        _ <- TelegramApiService.sendMessage(SendMessageRequest(text = text, replyMarkup = Some(keyboard), chatId = chatId))
      } yield ()
  }

  val live: ULayer[TelegramBotService] = ZLayer.succeed(new ServiceImpl)
}
