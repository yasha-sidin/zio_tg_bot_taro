package ru.otus

import client.TelegramClient

import ru.otus.configuration.Configuration
import ru.otus.dto.telegram.request.GetUpdatesRequest
import zio.config.typesafe.TypesafeConfigProvider
import zio.{ConfigProvider, Scope, ULayer, ZIO, ZLayer}
import zio.http.Client

object App {
  type AppEnvironment = TelegramClient.Service with Configuration with Scope with Client

  val buildEnv: ZLayer[ConfigProvider, Throwable, AppEnvironment] =
    Configuration.live >+> TelegramClient.live ++ Scope.default ++ Client.default

  private val provider: ConfigProvider = TypesafeConfigProvider.fromResourcePath()

  val app = TelegramClient
    .getUpdates(GetUpdatesRequest(None, None, None, None))
    .flatMap(res => ZIO.succeed(println(res)))
    .provideSomeLayer(buildEnv).provide(ZLayer.succeed(provider))
}
