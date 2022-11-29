package scalacommerce.persistence

import scalacommerce.model.User
import scalacommerce.model.fe.user.UserCreateRequest

trait UserRepo[F[_]] {

  def get(id: Int): F[Option[User]]
  def getAll(): F[List[User]]
  def update(user: User): F[Unit]
  def create(email: UserCreateRequest): F[Int]
}
