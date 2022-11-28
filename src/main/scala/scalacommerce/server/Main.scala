package scalacommerce.server

import cats.effect.{ExitCode, IO, IOApp, Resource}
import org.typelevel.log4cats.slf4j.Slf4jLogger
import scalacommerce.controller.UserController
import scalacommerce.persistence.{DatabaseIO, UserRepoImpl}
import scalacommerce.service.UserService

object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] = {

    val dbTransactor = DatabaseIO.transactor
    val userRepo     = UserRepoImpl

    (for {
      logger         <- Resource.eval(Slf4jLogger.create[IO])
      userService    = new UserService(userRepo, dbTransactor, logger)
      userController = new UserController(userService, logger)
      server         <- new WebServer().run(userController)
      _              <- Resource.eval(logger.info(s"Started server at ${server.address}"))
    } yield ())
      .use(_ => IO.never)
      .as(ExitCode.Success)

  }

}
