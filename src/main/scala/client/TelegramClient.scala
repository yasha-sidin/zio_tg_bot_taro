package ru.otus
package client

import dto.telegram.request.{GetUpdatesRequest, SendMessageRequest}
import dto.telegram.{Message, TelegramResponse, Update}
import dto.telegram.Message._
import dto.telegram.Update._
import dto.telegram.TelegramResponse._
import dto.telegram.request.GetUpdatesRequest._
import dto.telegram.request.SendMessageRequest._
import configuration.Configuration

import zio._
import zio.json._
import zio.http.{Body, Client, Request}
import zio.macros.accessible

@accessible
object TelegramClient {
  type TelegramClient = Service

  trait Service {
    def getUpdates(getUpdatesRequest: GetUpdatesRequest): ZIO[Client & Scope, Serializable, TelegramResponse[Update]]
    def sendMessage(
        sendMessageRequest: SendMessageRequest
    ): ZIO[Client & Scope, Serializable, Message]
  }

  private case class ServiceImpl(config: Configuration) extends Service {

    override def getUpdates(
        getUpdatesRequest: GetUpdatesRequest
    ): ZIO[Client & Scope, Serializable, TelegramResponse[Update]] = {
      ZIO
        .scoped {
          Client
            .streaming(
              Request.post(
                s"${config.telegram.apiUrl}/bot${config.telegram.botToken}/getUpdates",
                Body.fromString(getUpdatesRequest.toJson)
              )
            )
            .flatMap(_.body.asString)
        }.tap(body => ZIO.debug(body))
        .flatMap(body => ZIO.fromEither(body.fromJson[TelegramResponse[Update]]))
        .mapError(e => new RuntimeException(s"Failed to parse Update: ${e}"))
    }

    override def sendMessage(
        sendMessageRequest: SendMessageRequest
    ): ZIO[Client & Scope, Serializable, Message] = {
      ZIO
        .scoped {
          Client
            .streaming(
              Request.post(
                s"${config.telegram.apiUrl}/bot${config.telegram.botToken}/sendMessage",
                Body.fromString(sendMessageRequest.toJson)
              )
            )
            .flatMap(_.body.asString)
        }
        .flatMap(body => ZIO.fromEither(body.fromJson[Message]))
    }
  }

  val live: ZLayer[Configuration, Nothing, TelegramClient.Service] = ZLayer {
    for {
      config <- ZIO.service[Configuration]
    } yield ServiceImpl(config)
  }
}
