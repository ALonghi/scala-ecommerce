package scalacommerce.persistence

import doobie.free.connection.ConnectionIO

import java.util.UUID

trait UserRepo {

  def get(id: UUID): ConnectionIO[Option[String]]
  def getAll(): ConnectionIO[List[String]]
  def create(email: String): ConnectionIO[Int]
}
