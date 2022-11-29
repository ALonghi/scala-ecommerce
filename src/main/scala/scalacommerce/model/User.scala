package scalacommerce.model
import cats.Eq
import cats.syntax.eq._

case class User(id: Int, email: String, name: String, surname: String)

object User {
  // custom comparator with Eq type class (just to show off and make it extendable in the future)
  implicit val userComparator = Eq.instance[User] { (user1, user2) =>
    user1.name === user2.name &&
      user1.surname === user2.surname &&
      user1.email === user2.email
  }

}
