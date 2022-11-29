package scalacommerce.server

import cats.effect.{ ExitCode, IO, IOApp, Resource }
import org.typelevel.log4cats.slf4j.Slf4jLogger
import scalacommerce.controller.UserController
import scalacommerce.persistence.{ DatabaseConfig, DatabaseIO, FlywayMigration, UserRepoImpl }
import scalacommerce.service.UserService

object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] = {

    val dbTransactor = DatabaseIO.transactor
    val userRepo = new UserRepoImpl(dbTransactor)

    (for {
      logger <- Resource.eval(Slf4jLogger.create[IO])
      _ <- Resource.eval(logger.info(s"Db conf 1 is${DatabaseConfig.config}"))
      _ <- Resource.eval(logger.info(s"Db conf 2 is${DatabaseIO.config}"))
      _ <- Resource.eval(logger.info(s"Db kernel 2 is${dbTransactor.kernel}"))
      _ <- Resource.eval(FlywayMigration.migrate(DatabaseConfig.config, logger))
      userService = new UserService(userRepo, logger)
      userController = new UserController(userService, logger)
      server <- new WebServer().run(userController)
      _ <- Resource.eval(logger.info(s"Started server at ${server.address}"))
    } yield ())
      .use(_ => IO.never)
      .as(ExitCode.Success)

  }

}
