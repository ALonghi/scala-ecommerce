package scalacommerce.controller

import cats.MonadError
import cats.implicits.catsSyntaxApplicativeError
import cats.syntax.functor._
import org.typelevel.log4cats.Logger
import scalacommerce.model.User
import scalacommerce.model.fe.user.{ UserCreateRequest, UserCreateResponse, UserUpdateRequest, UserUpdateResponse }
import scalacommerce.service.UserService

class UserController[F[_]](userService: UserService[F], logger: Logger[F])(
    implicit
    me: MonadError[F, Throwable]
) {

  def get(id: Int): F[Option[User]] =
    userService.get(id).handleErrorWith {
      case e =>
        logger
          .error(s"Got error while fetching user with id $id: ${e.getMessage}")
          .map(_ => Option.empty[User])
    }

  def getAll(): F[List[User]] =
    userService.getAll().handleErrorWith {
      case e =>
        logger
          .error(s"Got error while fetching users: ${e.getMessage}")
          .map(_ => List.empty[User])
    }

  def update(r: UserUpdateRequest, userId: Int): F[UserUpdateResponse] =
    userService
      .update(r, userId)
      .map(_ => UserUpdateResponse(persisted = true))
      .handleErrorWith {
        case e =>
          val errorMsg = s"Got error while updating user with id $userId: ${e.getMessage}"
          logger
            .error(errorMsg)
            .map(_ => UserUpdateResponse(persisted = false, error = Some(errorMsg)))
      }

  def create(r: UserCreateRequest): F[UserCreateResponse] =
    userService
      .create(r)
      .map(id => UserCreateResponse(id = Some(id)))
      .handleErrorWith {
        case e =>
          val errorMsg = s"Got error while creating user ($r): ${e.getMessage}"
          logger
            .error(errorMsg)
            .map(_ => UserCreateResponse(error = Some(errorMsg)))
      }
}
