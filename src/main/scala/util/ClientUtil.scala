package ru.otus
package util

import io.circe.{Decoder, Encoder, Json}
import io.circe.parser.decode
import io.circe.syntax._
import zio.{&, RIO, Scope, ZIO}
import zio.http.{Body, Client, Request}

object ClientUtil {
  def requestPost[T, R](url: String, request: T, logResponse: Boolean)(implicit encoder: Encoder[T], decoder: Decoder[R]): RIO[Client & Scope, Option[R]] =
    (for {
      json <- ZIO.succeed(cleanJson(request.asJson).toString)
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

  private def cleanJson(json: Json): Json =
    json.mapObject(_.filter {
      case (_, Json.Null) => false
      case _ => true
    })
}
