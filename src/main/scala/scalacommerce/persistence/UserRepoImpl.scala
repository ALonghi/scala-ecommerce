package scalacommerce.persistence

import cats.effect.IO
import doobie.implicits._
import doobie.postgres.implicits._
import doobie.util.transactor.Transactor
import scalacommerce.model.User
import scalacommerce.model.fe.user.UserCreateRequest

class UserRepoImpl(xa: Transactor[IO]) extends UserRepo[IO] {

  override def get(id: Int): IO[Option[User]] =
    sql"""select * from users where id=$id""".query[User].option.transact(xa)

  override def getAll(): IO[List[User]] =
    sql"""select * from users""".query[User].to[List].transact(xa)

  override def update(user: User): IO[Unit] =
    sql"""
        update users
            set email = ${user.email},
            name = ${user.name},
            surname = ${user.surname}
        where id = ${user.id}
       """
      .update
      .run
      .transact(xa)
      .void

  override def create(r: UserCreateRequest): IO[Int] = {
    sql"""insert into users(email, name, surname)
         values (${r.email}, ${r.name}, ${r.surname})"""
      .update
      .withUniqueGeneratedKeys[Int]("id")
      .transact(xa)
  }

}
