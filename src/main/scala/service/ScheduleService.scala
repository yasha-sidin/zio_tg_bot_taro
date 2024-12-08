package ru.otus
package service

import configuration.{Configuration, ScheduleConfig}
import client.TelegramClient
import repository.TelegramOffsetRepository

import zio.http.Client
import zio.macros.accessible
import zio.{&, Schedule, URLayer, ZIO, ZLayer, durationLong}

import javax.sql.DataSource
import scala.language.postfixOps

@accessible
object ScheduleService {
  type ScheduleService = Service
  private type ScheduleServiceEnv = TelegramApiService.Service with TelegramClient.Service with Client & zio.Scope with TelegramBotService.Service with TelegramOffsetRepository.Service with DataSource

  trait Service {
    def runBot(): ZIO[ScheduleServiceEnv, Serializable, Unit]
  }

  private class ServiceImpl(config: ScheduleConfig) extends Service {
    override def runBot(): ZIO[ScheduleServiceEnv, Serializable, Unit] =
      for {
        botStartTime <- ZIO.succeed(System.currentTimeMillis() / 1000)
        _ <- TelegramApiService.resetUpdates()
        _ <- TelegramBotService.process(botStartTime) repeat Schedule.fixed(config.repeat milliseconds)
      } yield ()
  }

  val live: URLayer[Configuration, ScheduleService] = ZLayer {
    for {
      config <- ZIO.service[Configuration]
    } yield new ServiceImpl(config.schedule)
  }
}
