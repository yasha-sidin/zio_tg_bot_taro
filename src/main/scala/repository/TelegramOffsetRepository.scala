package ru.otus
package repository

import io.getquill.context.ZioJdbc.QIO
import dao.{TelegramOffset, TelegramOffsetId}

import io.getquill.{EntityQuery, Quoted}
import zio.macros.accessible
import zio.{ULayer, ZLayer}

@accessible
object TelegramOffsetRepository {
  private val dc: db.Ctx.type = db.Ctx

  import dc._

  type TelegramOffsetRepository = Service

  trait Service {
    def getOffsetById(id: TelegramOffsetId): QIO[Option[TelegramOffset]]
    def updateOffset(telegramOffset: TelegramOffset): QIO[Unit]
  }

  class ServiceImpl extends Service {
    private val telegramOffsetSchema: Quoted[EntityQuery[TelegramOffset]] =
      querySchema[TelegramOffset]("""offset_storage""")

    override def getOffsetById(telegramOffsetId: TelegramOffsetId): QIO[Option[TelegramOffset]] =
      dc.run(quote(telegramOffsetSchema.filter(_.id == lift(telegramOffsetId.id)).take(1)))
        .map(_.headOption)

    override def updateOffset(telegramOffset: TelegramOffset): QIO[Unit] =
      dc.run(
        quote(
          telegramOffsetSchema
            .filter(_.id == lift(telegramOffset.id))
            .updateValue(lift(telegramOffset))
        )
      ).unit
  }

  val live: ULayer[TelegramOffsetRepository] = ZLayer.succeed(new ServiceImpl)
}
