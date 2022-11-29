package scalacommerce.model.fe.user

case class UserUpdateResponse(
    persisted: Boolean,
    error:     Option[String] = None
)
