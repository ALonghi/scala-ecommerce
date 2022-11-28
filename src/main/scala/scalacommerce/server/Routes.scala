package scalacommerce.server

import cats.effect.IO
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.dsl.Http4sDsl
import scalacommerce.controller.UserController
import scalacommerce.model.UserRequest

object Routes {

  def userRoutes(users: UserController): HttpRoutes[IO] = {
    val dsl = new Http4sDsl[IO] {}
    import dsl._
    HttpRoutes.of[IO] {

      case GET -> Root / "healthy" => Ok()

      case GET -> Root / "ready" => Ok()

      case GET -> Root / "users" =>
        for {
          users <- users.getAll()
          resp  <- Ok(users)
        } yield resp

      case GET -> Root / "users" / UUIDVar(id) =>
        for {
          user <- users.get(id)
          resp <- Ok(user)
        } yield resp

      case req @ POST -> Root / "users" =>
        for {
          body <- req.as[UserRequest]
          _    <- users.create(body.email)
          resp <- Ok()
        } yield resp

    }
  }

}
