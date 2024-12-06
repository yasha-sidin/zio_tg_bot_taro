package ru.otus

import zio.{Config, ConfigProvider, ZIO, ZLayer}
import zio.config.magnolia._

package object configuration {
  case class AppConfig(
      telegram: TelegramConfig,
      postgres: PostgresConfig,
      liquibase: LiquibaseConfig,
      schedule: ScheduleConfig
  )

  type Configuration = AppConfig

  case class TelegramConfig(apiUrl: String, botToken: String)
  case class LiquibaseConfig(changeLog: String)
  case class PostgresConfig(url: String, databaseName: String, user: String, password: String)

  /**
    * @param repeat milliseconds
    */
  case class ScheduleConfig(repeat: Long)

  private val configDescriptor: Config[AppConfig] = deriveConfig[AppConfig]

  object Configuration {
    val live: ZLayer[ConfigProvider, Config.Error, Configuration] = ZLayer {
      ZIO.serviceWithZIO[ConfigProvider](provider => provider.load(configDescriptor))
    }
  }
}
