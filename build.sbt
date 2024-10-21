ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.15"

lazy val root = (project in file("."))
  .settings(
    name := "zio_tg_bot_taro",
    idePackagePrefix := Some("ru.otus")
  )

libraryDependencies ++= Dependencies.zio
libraryDependencies ++= Dependencies.zioTest
libraryDependencies ++= Dependencies.postgres
libraryDependencies ++= Dependencies.liquibase
libraryDependencies ++= Dependencies.scalaTest
libraryDependencies ++= Dependencies.enumeratum

scalacOptions += "-Ymacro-annotations"