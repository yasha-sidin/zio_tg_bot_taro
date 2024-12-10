package ru.otus
package repository

import io.getquill.context.ZioJdbc.QIO
import dao.{ChatId, ChatState, ChatStateId}

import io.getquill.{EntityQuery, Quoted}
import zio.{ULayer, ZLayer}
import zio.macros.accessible

@accessible
object ChatStateRepository {
  private val dc: db.Ctx.type = db.Ctx

  import dc._

  type ChatStateRepository = Service

  trait Service {
    def getStateById(id: ChatStateId): QIO[Option[ChatState]]
    def getStateByChatId(id: ChatId): QIO[Option[ChatState]]
    def updateState(state: ChatState): QIO[ChatState]
    def insertState(state: ChatState): QIO[ChatState]
  }

  private class ServiceImpl extends Service {
    private val chatStateSchema: Quoted[EntityQuery[ChatState]] =
      querySchema[ChatState]("""chat_state_storage""")

    private val schema = quote(chatStateSchema)

    override def getStateById(id: ChatStateId): QIO[Option[ChatState]] =
      dc.run(schema.filter(_.id == lift(id.id)).take(1)).map(_.headOption)

    override def getStateByChatId(id: ChatId): QIO[Option[ChatState]] =
      dc.run(schema.filter(_.chatId == lift(id.id)).take(1)).map(_.headOption)

    override def updateState(state: ChatState): QIO[ChatState] =
      dc.run(schema.filter(_.id == state.id).updateValue(lift(state))).as(state)

    override def insertState(state: ChatState): QIO[ChatState] =
      dc.run(schema.insertValue(lift(state))).as(state)
  }

  val live: ULayer[ChatStateRepository] = ZLayer.succeed(new ServiceImpl)
}
