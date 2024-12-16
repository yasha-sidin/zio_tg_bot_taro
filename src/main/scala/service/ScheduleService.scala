package ru.otus
package service

import configuration.{Configuration, ScheduleConfig}
import client.TelegramClient
import repository.{ChatStateRepository, TelegramOffsetRepository}

import ru.otus.service
import zio.http.Client
import zio.macros.accessible
import zio.stream.ZStream
import zio.{&, Schedule, Scope, URLayer, ZIO, ZLayer, durationLong}

import javax.sql.DataSource
import scala.language.postfixOps

@accessible
object ScheduleService {
  type ScheduleService = Service
  private type ScheduleServiceEnv =
    TelegramApiService.Service with TelegramClient.Service with Client & Scope with TelegramBotService.Service with ChatStateRepository.Service with ChatStateService.Service with TelegramOffsetRepository.Service with DataSource

  trait Service {
    def runBot(): ZIO[ScheduleServiceEnv, Serializable, Unit]
  }

  private class ServiceImpl(config: ScheduleConfig) extends Service {
    override def runBot(): ZIO[ScheduleServiceEnv, Serializable, Unit] =
      for {
        botStartTime <- ZIO.succeed(System.currentTimeMillis() / 1000)
        _ <- TelegramApiService.resetUpdates()
        _ <- ZStream
          .repeatZIO(TelegramBotService.process(botStartTime))
          .schedule(Schedule.fixed(config.repeat milliseconds))
          .runDrain
      } yield ()
  }

  val live: URLayer[Configuration, ScheduleService] = ZLayer {
    for {
      config <- ZIO.service[Configuration]
    } yield new ServiceImpl(config.schedule)
  }
}
