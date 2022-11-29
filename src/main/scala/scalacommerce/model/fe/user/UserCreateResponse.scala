package scalacommerce.model.fe.user

case class UserCreateResponse(
    id:    Option[Int]    = None,
    error: Option[String] = None
)
