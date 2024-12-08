package ru.otus
package service

import `type`.EnrollCommandState
import dao.{ChatId, EnrollState, EnrollStateId}
import repository.EnrollStateRepository

import zio.{RIO, Random, ULayer, ZIO, ZLayer}
import zio.macros.accessible

import javax.sql.DataSource

@accessible
object EnrollStateService {

  type EnrollStateService = Service

  type EnrollStateServiceEnv = EnrollStateRepository.Service with DataSource


  trait Service {
    def getStateById(id: String): RIO[EnrollStateServiceEnv, Option[EnrollState]]
    def getOrCreateStateByChatId(chatId: Long): RIO[EnrollStateServiceEnv, EnrollState]
    def updateState(state: EnrollState): RIO[EnrollStateServiceEnv, EnrollState]
  }

  private class ServiceImpl extends Service {

    override def getStateById(id: String): RIO[EnrollStateServiceEnv, Option[EnrollState]] =
      EnrollStateRepository.getStateById(EnrollStateId(id)).mapError(er => new Throwable(s"Error in getting state by id: $er"))

    override def getOrCreateStateByChatId(chatId: Long): RIO[EnrollStateServiceEnv, EnrollState] = for {
      stateOpt <- EnrollStateRepository.getStateByChatId(ChatId(chatId)).mapError(er => new Throwable(s"Error in getting state by chatId: $er"))
      state <- (stateOpt match {
        case Some(value) => ZIO.succeed(value)
        case None => for {
          uuid <- Random.nextUUID.map(_.toString)
          trx <- EnrollStateRepository.insertState(EnrollState(uuid, chatId, EnrollCommandState.MonthSelection))
        } yield trx
      }).mapError(er => new Throwable(s"Error in inserting state: $er"))
    } yield state

    override def updateState(state: EnrollState): RIO[EnrollStateServiceEnv, EnrollState] =
      EnrollStateRepository.updateState(state).mapError(er => new Throwable(s"Error in updating state: $er"))
  }

  val live: ULayer[EnrollStateService] = ZLayer.succeed(new ServiceImpl)
}
