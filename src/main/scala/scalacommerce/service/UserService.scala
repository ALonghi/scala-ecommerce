package scalacommerce.service

import cats.{ Monad, MonadError }
import cats.syntax.eq._
import cats.syntax.flatMap._
import cats.syntax.functor._
import org.typelevel.log4cats.Logger
import scalacommerce.model.User
import scalacommerce.model.User.userComparator
import scalacommerce.model.fe.user.{ UserCreateRequest, UserUpdateRequest }
import scalacommerce.persistence.UserRepo

class UserService[F[_]: Monad](userRepo: UserRepo[F], logger: Logger[F])(
    implicit
    me: MonadError[F, Throwable]
) {

  def get(id: Int): F[Option[User]] =
    for {
      _ <- logger.info(s"Fetching user with ID = $id...")
      result <- userRepo.get(id)
      _ <- logger.info(s"Fetched user with ID = $id, email = $result.")
    } yield result

  def getAll(): F[List[User]] =
    for {
      _ <- logger.info(s"Fetching all users...")
      result <- userRepo.getAll()
      _ <- logger.info(s"Fetched all users.")
    } yield result

  def update(r: UserUpdateRequest, userId: Int): F[Unit] = {
    def validateRequest(newUser: User): F[Unit] =
      userRepo
        .get(userId)
        .map(_ match {
          case Some(user) =>
            if (user === newUser) me.raiseError(new Exception("Can't update user with same data"))
            else Monad[F].unit
          case None => me.raiseError(new Exception("Can't update non existing user"))
        })

    for {
      _ <- logger.info(s"Updating user $userId...")
      user = User(userId, r.email, r.name, r.surname)
      _ <- validateRequest(user)
      result <- userRepo.update(user)
      _ <- logger.info(s"Updated user with ID = $userId.")
    } yield result
  }

  def create(r: UserCreateRequest): F[Int] =
    for {
      _ <- logger.info(s"Creating user ($r)...")
      id <- userRepo.create(r)
      _ <- logger.info(s"Created user ID = $id.")
    } yield id
}
