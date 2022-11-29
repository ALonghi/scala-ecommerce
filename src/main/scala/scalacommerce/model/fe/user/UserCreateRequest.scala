package scalacommerce.model.fe.user

case class UserCreateRequest(
    email:   String,
    name:    String,
    surname: String
)
