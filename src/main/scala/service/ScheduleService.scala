package ru.otus
package service

import configuration.{Configuration, ScheduleConfig}
import client.TelegramClient
import repository.TelegramOffsetRepository

import ru.otus.`type`.BotCommandType
import zio.http.Client
import zio.macros.accessible
import zio.{&, Schedule, URLayer, ZIO, ZLayer, durationLong}

import javax.sql.DataSource
import scala.language.postfixOps

@accessible
object ScheduleService {
  type ScheduleService = Service
  private type ScheduleServiceEnv = TelegramService.Service with TelegramClient.Service with Client & zio.Scope with TelegramOffsetRepository.Service with DataSource

  trait Service {
    def runBot(): ZIO[ScheduleServiceEnv, Serializable, Unit]
  }

  class ServiceImpl(config: ScheduleConfig) extends Service {
    override def runBot(): ZIO[ScheduleServiceEnv, Serializable, Unit] =
      for {
        botStartTime <- ZIO.succeed(System.currentTimeMillis() / 1000)
        _ <- TelegramService.checkCommands(BotCommandType.values.map(_.value).toList)
        _ <- TelegramService.resetUpdates()
        _ <- TelegramService.process(botStartTime) repeat Schedule.fixed(config.repeat milliseconds)
      } yield ()
  }

  val live: URLayer[Configuration, ServiceImpl] = ZLayer {
    for {
      config <- ZIO.service[Configuration]
    } yield new ServiceImpl(config.schedule)
  }
}
