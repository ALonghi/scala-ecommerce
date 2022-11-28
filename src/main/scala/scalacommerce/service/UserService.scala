package scalacommerce.service

import cats.effect.IO
import doobie.implicits._
import doobie.util.transactor.Transactor
import org.typelevel.log4cats.Logger
import scalacommerce.persistence.UserRepo

import java.util.UUID

class UserService(userRepo: UserRepo, dbTransactor: Transactor[IO], logger: Logger[IO]) {

  def get(id: UUID): IO[Option[String]] =
    for {
      _      <- logger.info(s"Fetching user with ID = $id...")
      result <- userRepo.get(id).transact(dbTransactor)
      _      <- logger.info(s"Fetched user with ID = $id, email = $result.")
    } yield result

  def getAll(): IO[List[String]] =
    for {
      _      <- logger.info(s"Fetching all users...")
      result <- userRepo.getAll.transact(dbTransactor)
      _      <- logger.info(s"Fetched all users.")
    } yield result

  def create(email: String): IO[Int] =
    for {
      _      <- logger.info(s"Creating user with email = $email...")
      result <- userRepo.create(email).transact(dbTransactor)
      _      <- logger.info(s"Created user ID = $email.")
    } yield result
}
