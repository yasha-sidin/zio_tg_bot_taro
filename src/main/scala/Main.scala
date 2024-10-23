package ru.otus

import App.app

import zio.{Schedule, Scope, ZIO, ZIOAppArgs, ZIOAppDefault, durationInt}

import scala.language.postfixOps

object Main extends ZIOAppDefault {
  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] =
    ZIO.log("App started...") *> app.repeat(Schedule.fixed(1 seconds))
}
