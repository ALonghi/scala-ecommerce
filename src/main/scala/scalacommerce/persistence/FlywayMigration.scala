package scalacommerce.persistence

import cats.effect.IO
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.output.MigrateResult
import org.typelevel.log4cats.Logger

object FlywayMigration {

  def migrate(config: DatabaseConfig, logger: Logger[IO]): IO[Unit] = {
    def executeMigration: MigrateResult =
      Flyway
        .configure()
        .dataSource(config.url, config.username, config.password)
        .locations("db/migration")
        .baselineOnMigrate(true)
        .load()
        .migrate()
    (for {
      _ <- logger.info("Starting flyway migration..")
      _ <- IO(executeMigration)
      _ <- logger.info("Flyway migration finished.")
    } yield ())
      .handleErrorWith(e =>
        logger
          .error(s"Flyway migration exception: ${e.getMessage}")
          .flatMap(_ => IO.raiseError(e)))
  }

}
