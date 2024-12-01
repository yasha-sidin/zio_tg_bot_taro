package ru.otus

import client.TelegramClient
import configuration.Configuration
import dto.telegram.request.GetUpdatesRequest
import configuration.CirceConfig.CirceError
import dto.telegram.{TelegramResponse, Update}
import db.{DataSource, LiquibaseService, zioDS}

import zio.config.typesafe.TypesafeConfigProvider
import zio.{ConfigProvider, RIO, RLayer, Scope, ZLayer}
import zio.http.Client

object App {
  private type AppEnvironment = TelegramClient.Service
    with Configuration
    with LiquibaseService.Liqui
    with LiquibaseService.Service
    with Scope
    with Client

  private val buildEnv: RLayer[ConfigProvider, AppEnvironment] =
    Configuration.live >+> TelegramClient.live >+> zioDS >+> Scope.default >+> LiquibaseService.liquibaseLayer ++ LiquibaseService.live ++ Client.default

  private val provider: ConfigProvider = TypesafeConfigProvider.fromResourcePath()

  val app: RIO[Any, Unit] = LiquibaseService
    .performMigration
    .provideSomeLayer(buildEnv)
    .provide(ZLayer.succeed(provider))
}
