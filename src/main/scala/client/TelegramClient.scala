package ru.otus
package client

import dto.telegram.request.{GetUpdatesRequest, SendMessageRequest}
import dto.telegram.{Message, TelegramResponse, Update}
import configuration.{Configuration, TelegramConfig}
import configuration.CirceConfig._

import zio._
import zio.http.{Body, Client, Request}
import zio.macros.accessible
import io.circe.generic.extras.auto._
import io.circe.syntax._
import io.circe.parser._

@accessible
object TelegramClient {

  type TelegramClient = Service

  trait Service {
    def getUpdates(
        getUpdatesRequest: GetUpdatesRequest
    ): RIO[Client & Scope, Either[CirceError, TelegramResponse[Update]]]
    def sendMessage(
        sendMessageRequest: SendMessageRequest
    ): RIO[Client & Scope, Either[CirceError, Message]]
  }

  private case class ServiceImpl(config: TelegramConfig) extends Service {
    private val mainUrl = s"${config.apiUrl}/bot${config.botToken}"

    override def getUpdates(
        getUpdatesRequest: GetUpdatesRequest
    ): RIO[Client & Scope, Either[CirceError, TelegramResponse[Update]]] = {
      ZIO
        .scoped {
          Client
            .streaming(
              Request.post(
                s"$mainUrl/getUpdates",
                Body.fromString(getUpdatesRequest.asJson.toString())
              )
            )
            .flatMap(_.body.asString)
        }
        .flatMap(body => ZIO.succeed(decode[TelegramResponse[Update]](body)))
    }

    override def sendMessage(
        sendMessageRequest: SendMessageRequest
    ): RIO[Client & Scope, Either[CirceError, Message]] = {
      ZIO
        .scoped {
          Client
            .streaming(
              Request.post(
                s"$mainUrl/sendMessage",
                Body.fromString(sendMessageRequest.asJson.toString())
              )
            )
            .flatMap(_.body.asString)
        }
        .flatMap(body => ZIO.succeed(decode[Message](body)))
    }
  }

  val live: ZLayer[Configuration, Nothing, TelegramClient.Service] = ZLayer {
    for {
      config <- ZIO.service[Configuration]
    } yield ServiceImpl(config.telegram)
  }
}
