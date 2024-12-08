package ru.otus

import client.TelegramClient
import configuration.Configuration
import db.{LiquibaseService, zioDS}
import repository.TelegramOffsetRepository
import service.{ScheduleService, TelegramBotService, TelegramApiService}

import zio.config.typesafe.TypesafeConfigProvider
import zio.{Scope, ZIO, ZLayer}
import zio.http.Client

object App {
  private val configProvider = ZLayer.succeed(TypesafeConfigProvider.fromResourcePath())

  private val buildEnv =
    configProvider >+> Configuration.live >+> TelegramClient.live >+> zioDS >+> Scope.default >+>
      LiquibaseService.liquibaseLayer ++ TelegramOffsetRepository.live >+> TelegramApiService.live >+>
      TelegramBotService.live >+> ScheduleService.live ++ LiquibaseService.live ++ Client.default

  private val build = for {
    _ <- LiquibaseService.performMigration
    fork <- ScheduleService.runBot().fork
    _ <- ZIO.logInfo("Bot started...")
    _ <- fork.join
  } yield ()

  val app: ZIO[Any, Any, Unit] = build.provideSomeLayer(buildEnv)
}
