package ru.otus

import client.TelegramClient
import configuration.Configuration
import db.{LiquibaseService, zioDS}

import dao.TelegramOffsetId
import repository.TelegramOffsetRepository
import zio.config.typesafe.TypesafeConfigProvider
import zio.{Scope, ZIO, ZLayer}
import zio.http.Client

object App {
  private val configProvider = ZLayer.succeed(TypesafeConfigProvider.fromResourcePath())

  private val buildEnv =
    configProvider >+> Configuration.live >+> TelegramClient.live >+> zioDS >+> Scope.default >+>
      LiquibaseService.liquibaseLayer ++ TelegramOffsetRepository.live ++ LiquibaseService.live ++ Client.default

  private val build = for {
    _      <- LiquibaseService.performMigration
    offset <- TelegramOffsetRepository.getOffsetById(TelegramOffsetId(0))
    _      <- ZIO.logInfo(s"Offset value from db: $offset")
  } yield ()

  val app: ZIO[Any, Any, Unit] = build.provideSomeLayer(buildEnv)
}
