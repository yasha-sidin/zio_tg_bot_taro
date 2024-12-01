import sbt.*

object Dependencies {

  /**
    * ZIO main
    */
  val ZioVersion        = "2.1.6"
  val ZioHttpVersion    = "3.0.1"
  val ZioConfigVersion  = "4.0.2"
  val ZioJsonVersion    = "0.7.3"
  val ZioQuillVersion   = "4.8.4"
  val ZioRedisVersion   = "1.0.0"
  val ZioLoggingVersion = "2.3.2"

  /**
    * ZIO
    */
  lazy val zio: Seq[ModuleID] = Seq(
    "dev.zio"     %% "zio"                 % ZioVersion,
    "dev.zio"     %% "zio-macros"          % ZioVersion,
    "dev.zio"     %% "zio-config"          % ZioConfigVersion,
    "dev.zio"     %% "zio-config-typesafe" % ZioConfigVersion,
    "dev.zio"     %% "zio-config-magnolia" % ZioConfigVersion,
    "dev.zio"     %% "zio-config-refined"  % ZioConfigVersion,
    "dev.zio"     %% "zio-http"            % ZioHttpVersion,
    "dev.zio"     %% "zio-json"            % ZioJsonVersion,
    "dev.zio"     %% "zio-json-macros"     % ZioJsonVersion,
    "dev.zio"     %% "zio-logging"         % ZioLoggingVersion,
    "dev.zio"     %% "zio-logging-slf4j"   % ZioLoggingVersion,
    "io.getquill" %% "quill-jdbc-zio"      % ZioQuillVersion,
    "dev.zio"     %% "zio-redis"           % ZioRedisVersion
  )

  /**
    * ZIO test
    */
  val zioMockVersion      = "1.0.0-RC12"
  val zioRedisTestVersion = "1.0.0"
  lazy val zioTest: Seq[ModuleID] = Seq(
    "dev.zio" %% "zio-test"           % ZioVersion,
    "dev.zio" %% "zio-test-sbt"       % ZioVersion,
    "dev.zio" %% "zio-redis-embedded" % zioRedisTestVersion,
    "dev.zio" %% "zio-mock"           % zioMockVersion
  )

  /**
    * Postgres
    */
  val postgresVersion = "42.7.3"
  lazy val postgres: Seq[ModuleID] = Seq(
    "org.postgresql" % "postgresql" % postgresVersion
  )

  /**
    * Liquibase
    */
  val liquibaseVersion = "4.29.0"
  lazy val liquibase: Seq[ModuleID] = Seq(
    "org.liquibase"     % "liquibase-core"       % liquibaseVersion,
    "org.liquibase.ext" % "liquibase-postgresql" % liquibaseVersion
  )

  /**
    * Scala test
    */
  val scalaTestVersion = "3.2.19"
  lazy val scalaTest: Seq[ModuleID] = Seq(
    "org.scalatest" %% "scalatest" % scalaTestVersion
  )

  /**
    * Enumeratum
    */
  val enumeratumVersion = "1.7.5"
  lazy val enumeratum: Seq[ModuleID] = Seq(
    "com.beachape" %% "enumeratum" % enumeratumVersion
  )

  /**
    * Circe
    */
  val CirceVersion = "0.14.9"
  val CirceGenericExtrasVersion = "0.14.4"
  val circe: Seq[ModuleID] = Seq(
    "io.circe" %% "circe-core"           % CirceVersion,
    "io.circe" %% "circe-generic"        % CirceVersion,
    "io.circe" %% "circe-parser"         % CirceVersion,
    "io.circe" %% "circe-generic-extras" % CirceGenericExtrasVersion
  )
}
