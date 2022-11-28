package scalacommerce.model

final case class UserResponse(
    id: Option[Int] = None,
    error: Option[String] = None
)
