package scalacommerce.persistence

import cats.effect.IO
import doobie.util.transactor.Transactor

object DatabaseIO {

  val config = DatabaseConfig.config

  def transactor =
    Transactor.fromDriverManager[IO](
      config.driver,
      config.url,
      config.username,
      config.password
    )

}
