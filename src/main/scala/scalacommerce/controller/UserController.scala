package scalacommerce.controller

import cats.MonadError
import cats.effect.IO
import org.typelevel.log4cats.Logger
import scalacommerce.model.UserResponse
import scalacommerce.service.UserService

import java.util.UUID

class UserController(userService: UserService, logger: Logger[IO])(
    implicit me: MonadError[IO, Throwable]
) {

  def get(id: UUID): IO[Option[String]] =
    userService.get(id).handleErrorWith {
      case e =>
        logger
          .error(s"Got error while fetching user with id $id: ${e.getMessage}")
          .map(_ => Option.empty[String])
    }

  def getAll(): IO[List[String]] =
    userService.getAll.handleErrorWith {
      case e =>
        logger
          .error(s"Got error while fetching users: ${e.getMessage}")
          .map(_ => List.empty[String])
    }

  def create(email: String): IO[UserResponse] =
    userService
      .create(email)
      .map(id => UserResponse(id = Some(id)))
      .handleErrorWith {
        case e =>
          val errorMsg = s"Got error while creating user with email $email: ${e.getMessage}"
          logger
            .error(errorMsg)
            .map(_ => UserResponse(error = Some(errorMsg)))
      }
}
