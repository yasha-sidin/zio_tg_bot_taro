package ru.otus

import com.zaxxer.hikari.HikariDataSource
import io.getquill.{Literal, NamingStrategy, PostgresZioJdbcContext, SnakeCase}
import configuration.{Configuration, LiquibaseConfig, PostgresConfig}

import liquibase.Liquibase
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.{ClassLoaderResourceAccessor, CompositeResourceAccessor, SearchPathResourceAccessor}
import zio.macros.accessible
import zio.{RIO, ULayer, URIO, URLayer, ZIO, ZLayer}

import javax.sql.{DataSource => Source}

package object db {
  type DataSource = Source

  object Ctx extends PostgresZioJdbcContext(NamingStrategy(SnakeCase, Literal))

  private def hikariDS(config: PostgresConfig): HikariDataSource = {
    val dataSource = new HikariDataSource()
    dataSource.setJdbcUrl(s"jdbc:postgresql://${config.url}/${config.databaseName}")
    dataSource.setUsername(config.user)
    dataSource.setPassword(config.password)
    dataSource
  }

  val zioDS: URLayer[Configuration, DataSource] = ZLayer {
    for {
      config <- ZIO.service[Configuration]
    } yield hikariDS(config.postgres)
  }

  @accessible
  object LiquibaseService {
    type LiquibaseService = Service

    type Liqui = Liquibase

    trait Service {
      def performMigration: RIO[Liqui, Unit]
    }

    private class Impl extends Service {

      override def performMigration: RIO[Liqui, Unit] = liquibase.map(_.update("dev"))
    }

    private def mkLiquibase(liquibaseConfig: LiquibaseConfig) =
      for {
        ds <- ZIO.environment[DataSource].map(_.get)
        fileAccessor <- ZIO.attempt(
          new SearchPathResourceAccessor(
            s"${System.getProperty("user.dir")}\\${liquibaseConfig.changeLog}"
          )
        )
        classLoader         <- ZIO.attempt(classOf[LiquibaseService].getClassLoader)
        classLoaderAccessor <- ZIO.attempt(new ClassLoaderResourceAccessor(classLoader))
        fileOpener          <- ZIO.attempt(new CompositeResourceAccessor(fileAccessor, classLoaderAccessor))
        jdbcConn <- ZIO.acquireRelease(ZIO.attempt(new JdbcConnection(ds.getConnection())))(c =>
          ZIO.succeed(c.close())
        )
        liqui <- ZIO.attempt(new Liquibase("main.xml", fileOpener, jdbcConn))
      } yield liqui

    val liquibaseLayer = ZLayer {
      for {
        config <- ZIO.service[Configuration]
        liqui  <- mkLiquibase(config.liquibase)
      } yield liqui
    }

    def liquibase: URIO[Liquibase, LiquibaseService.Liqui] = ZIO.service[Liqui]

    val live: ULayer[Service] = ZLayer.succeed(new Impl)
  }
}
