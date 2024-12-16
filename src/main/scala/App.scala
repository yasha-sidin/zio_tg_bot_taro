package ru.otus

import client.TelegramClient
import configuration.Configuration
import db.{LiquibaseService, zioDS}
import repository.{ChatStateRepository, TelegramOffsetRepository}
import service.{ChatStateService, ScheduleService, TelegramApiService, TelegramBotService}

import zio.config.typesafe.TypesafeConfigProvider
import zio.{Scope, ZIO, ZLayer}
import zio.http.Client

object App {
  private val configProvider = ZLayer.succeed(TypesafeConfigProvider.fromResourcePath())

  private val buildEnv =
    configProvider >+> Configuration.live >+> TelegramClient.live >+> zioDS >+> Scope.default >+>
      LiquibaseService.liquibaseLayer ++ TelegramOffsetRepository.live >+> TelegramApiService.live >+>
      TelegramBotService.live >+> ChatStateRepository.live >+> ChatStateService.live >+> ScheduleService.live ++ LiquibaseService.live ++ Client.default

  private val build = for {
    _ <- LiquibaseService.performMigration
    _ <- ScheduleService.runBot()
  } yield ()

  val app: ZIO[Any, Any, Unit] = build.provideSomeLayer(buildEnv)
}
