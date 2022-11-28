package scalacommerce.server

import cats.effect.{IO, Resource}
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.implicits._
import org.http4s.server.Server
import scalacommerce.controller.UserController

class WebServer {

  def run(userController: UserController): Resource[IO, Server] = {

    val httpApp = Routes.userRoutes(userController).orNotFound

    BlazeServerBuilder[IO]
      .bindHttp(8080, "0.0.0.0")
      .withHttpApp(httpApp)
      .resource

  }

}
