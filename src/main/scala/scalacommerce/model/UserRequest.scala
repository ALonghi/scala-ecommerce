package scalacommerce.model

import cats.effect.IO
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import org.http4s.circe._

final case class UserRequest(email: String)

object UserRequest {

  implicit val decoder: Decoder[UserRequest] = deriveDecoder[UserRequest]
  implicit val encoder: Encoder[UserRequest] = deriveEncoder[UserRequest]
  implicit val entityDecoder                 = jsonOf[IO, UserRequest]
}
