package scalacommerce.server

import cats.effect.IO
import cats.effect.unsafe.IORuntime
import cats.effect.unsafe.IORuntime.global
import io.circe.{ Encoder, Printer }
import io.circe.generic.auto._
import org.http4s.EntityEncoder
import org.http4s.circe._
import org.http4s.rho.RhoRoutes
import org.http4s.rho.swagger.SwaggerSyntax
import scalacommerce.controller.UserController
import scalacommerce.model.fe.user.{ UserCreateRequest, UserUpdateRequest }

class Routes(
    swaggerSyntax: SwaggerSyntax[IO],
    users:         UserController[IO]
) {

  implicit val ioRuntime: IORuntime = global

  import swaggerSyntax._
  implicit def circeEntityEncoder[F[_], A: Encoder]: EntityEncoder[F, A] =
    jsonEncoderWithPrinterOf(Printer.noSpaces.copy(dropNullValues = true))

  val routes: RhoRoutes[IO] = new RhoRoutes[IO] {

    "Get users" ** "Users" @@ GET / "api" / "users" |>> users.getAll()

    "Get user by id" ** "Users" @@ GET / "api" / "users" / pathVar[Int]("id") |>> { (id: Int) => users.get(id) }

    ("Create a user" ** "Users" @@ POST / "api" / "users")
      .decoding(jsonOf[IO, UserCreateRequest]) |>> { (r: UserCreateRequest) => users.create(r) }

    ("Update a user" ** "Users" @@ POST / "api" / "users" / pathVar[Int]("id"))
      .decoding(jsonOf[IO, UserUpdateRequest]) |>> { (id: Int, r: UserUpdateRequest) => users.update(r, id) }

  }
}
