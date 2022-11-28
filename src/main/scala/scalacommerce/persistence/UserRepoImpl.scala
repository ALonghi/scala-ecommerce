package scalacommerce.persistence

import doobie.free.connection.ConnectionIO
import doobie.implicits._

import java.util.UUID

object UserRepoImpl extends UserRepo {

  override def get(id: UUID): ConnectionIO[Option[String]] =
    sql"""select email from users where id=$id""".query[Option[String]].unique

  override def getAll(): ConnectionIO[List[String]] =
    sql"""select email from users""".query[String].to[List]

  override def create(email: String): ConnectionIO[Int] =
    sql"""insert into users(id, email) values (${UUID.randomUUID()}, $email)"""
      .update
      .withUniqueGeneratedKeys[Int]("id")

}
