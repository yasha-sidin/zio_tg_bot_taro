package ru.otus
package service

import `type`.ChatStateType
import dao.{ChatId, ChatState, ChatStateId}
import repository.ChatStateRepository

import zio.{RIO, Random, ULayer, ZIO, ZLayer}
import zio.macros.accessible

import java.util.UUID
import javax.sql.DataSource

@accessible
object ChatStateService {

  type ChatStateService = Service

  private type ChatStateServiceEnv = ChatStateRepository.Service with DataSource

  trait Service {
    def getStateById(id: UUID): RIO[ChatStateServiceEnv, Option[ChatState]]
    def getOrCreateStateByChatId(chatId: Long): RIO[ChatStateServiceEnv, ChatState]
    def updateState(state: ChatState): RIO[ChatStateServiceEnv, ChatState]
  }

  private class ServiceImpl extends Service {

    override def getStateById(id: UUID): RIO[ChatStateServiceEnv, Option[ChatState]] =
      ChatStateRepository.getStateById(ChatStateId(id)).mapError(er => new Throwable(s"Error in getting state by id: $er"))

    override def getOrCreateStateByChatId(chatId: Long): RIO[ChatStateServiceEnv, ChatState] = for {
      stateOpt <- ChatStateRepository.getStateByChatId(ChatId(chatId)).mapError(er => new Throwable(s"Error in getting state by chatId: $er"))
      state <- (stateOpt match {
        case Some(value) => ZIO.succeed(value)
        case None => for {
          uuid <- Random.nextUUID
          trx <- ChatStateRepository.insertState(ChatState(uuid, chatId, ChatStateType.MainMenu))
        } yield trx
      }).mapError(er => new Throwable(s"Error in inserting state: $er"))
    } yield state

    override def updateState(state: ChatState): RIO[ChatStateServiceEnv, ChatState] =
      ChatStateRepository.updateState(state).mapError(er => new Throwable(s"Error in updating state: $er"))
  }

  val live: ULayer[ChatStateService] = ZLayer.succeed(new ServiceImpl)
}
