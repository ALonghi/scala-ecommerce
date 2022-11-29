package scalacommerce.server

import cats.effect.{ IO, Resource }
import cats.syntax.semigroupk._
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.implicits._
import org.http4s.rho.RhoMiddleware
import org.http4s.rho.swagger.syntax.{ io => ioSwagger }
import org.http4s.server.Server
import scalacommerce.controller.UserController

import scala.annotation.nowarn

class WebServer {

  @nowarn
  def run(userController: UserController[IO]): Resource[IO, Server] = {

    val (swaggerMiddleware, swaggerRoutes): (RhoMiddleware[IO], RhoMiddleware[IO]) =
      CustomSwaggerSupport.apply[IO].createRhoMiddleware
    val publicRoutes = new Routes(ioSwagger, userController).routes

    val allRoutes = publicRoutes.toRoutes(swaggerMiddleware) <+> publicRoutes.toRoutes(swaggerRoutes)

    BlazeServerBuilder[IO]
      .bindHttp(8080, "0.0.0.0")
      .withHttpApp(allRoutes.orNotFound)
      .resource

  }

}
