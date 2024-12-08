package ru.otus
package repository

import io.getquill.context.ZioJdbc.QIO
import dao.{ChatId, EnrollState, EnrollStateId}

import io.getquill.{EntityQuery, Quoted}
import zio.{ULayer, ZLayer}
import zio.macros.accessible

@accessible
object EnrollStateRepository {
  private val dc: db.Ctx.type = db.Ctx

  import dc._

  type EnrollStateRepository = Service

  trait Service {
    def getStateById(id: EnrollStateId): QIO[Option[EnrollState]]
    def getStateByChatId(id: ChatId): QIO[Option[EnrollState]]
    def updateState(state: EnrollState): QIO[EnrollState]
    def insertState(state: EnrollState): QIO[EnrollState]
  }

  private class ServiceImpl extends Service {
    private val enrollStateSchema: Quoted[EntityQuery[EnrollState]] =
      querySchema[EnrollState]("""enroll_command_state""")

    private val schema = quote(enrollStateSchema)

    override def getStateById(id: EnrollStateId): QIO[Option[EnrollState]] =
      dc.run(schema.filter(_.id == lift(id.id)).take(1)).map(_.headOption)

    override def getStateByChatId(id: ChatId): QIO[Option[EnrollState]] =
      dc.run(schema.filter(_.chatId == lift(id.id)).take(1)).map(_.headOption)

    override def updateState(state: EnrollState): QIO[EnrollState] =
      dc.run(schema.filter(_.id == state.id).updateValue(lift(state))).as(state)

    override def insertState(state: EnrollState): QIO[EnrollState] =
      dc.run(schema.insertValue(lift(state))).as(state)
  }

  val live: ULayer[EnrollStateRepository] = ZLayer.succeed(new ServiceImpl)
}
