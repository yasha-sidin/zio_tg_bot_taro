package ru.otus
package util

import io.circe.{Decoder, Encoder, Json, JsonObject}
import io.circe.parser.decode
import io.circe.syntax._
import zio.{&, RIO, Scope, ZIO}
import zio.http.{Body, Client, Request}

object ClientUtil {
  def requestPost[T, R](url: String, request: T, logResponse: Boolean, logPostBody: Boolean)(implicit encoder: Encoder[T], decoder: Decoder[R]): RIO[Client & Scope, Option[R]] =
    (for {
      json <- ZIO.succeed(cleanJson(request.asJson).toString)
      _ <- ZIO.log(json).when(logPostBody)
      respBody <-
        ZIO.scoped {
            Client.streaming(
                Request.post(url, Body.fromString(json))
                  .addHeader("Content-Length", json.length.toString)
                  .addHeader("Content-Type", "application/json")
              )
              .flatMap(_.body.asString)
          }
          .tap(resp => ZIO.log(resp).when(logResponse))
      decoded <- ZIO.fromEither(decode[R](respBody))
    } yield Some(decoded))
      .catchAll(er => ZIO.logError(er.getMessage).as(None))

  private def cleanJson(json: Json): Json = json match {
    case _ if json.isNull => Json.Null
    case _ if json.isObject =>
      Json.fromJsonObject(
        json.asObject.map(obj =>
          JsonObject.fromIterable(
            obj.toIterable.collect {
              case (key, value) if !value.isNull => key -> cleanJson(value)
            }
          )
        ).getOrElse(JsonObject.empty)
      )
    case _ if json.isArray =>
      Json.fromValues(
        json.asArray.map(_.map(cleanJson).filterNot(_.isNull)).getOrElse(Seq.empty)
      )
    case other => other
  }
}
