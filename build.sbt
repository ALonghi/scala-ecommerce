val Http4sVersion         = "0.23.12"
val CirceVersion          = "0.14.3"
val Log4CatsVersion       = "2.5.0"
val DoobieVersion         = "1.0.0-RC2"
val Slf4jVersion          = "2.0.4"
val PureConfigVersion     = "0.17.2"
val FlywayVersion         = "2.7.1"

enablePlugins(FlywayPlugin)

lazy val flywaySettings = Seq(
  flywayUrl := "jdbc:postgresql://localhost:5432/typelevelstack",
  flywayUser := "postgres",
  flywayPassword := "1234",
  flywayLocations += "db/migration",
  flywaySchemas += "public"
)

lazy val root = (project in file("."))
  .enablePlugins(FlywayPlugin)
  .settings(flywaySettings)
  .settings(
    organization := "scalacommerce",
    name := "scala-ecommerce",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.13.10",
    libraryDependencies ++= Seq(
      "io.circe"              %% "circe-generic"       % CirceVersion,
      "org.typelevel" %% "log4cats-slf4j" % Log4CatsVersion,
      "org.slf4j"             % "slf4j-simple"         % Slf4jVersion,
      "org.tpolecat"          %% "doobie-core"         % DoobieVersion,
      "org.tpolecat"          %% "doobie-postgres"     % DoobieVersion,
      "com.github.pureconfig" %% "pureconfig"          % PureConfigVersion,
      "org.hsqldb"            % "hsqldb"               % FlywayVersion
    ) ++ httpDependencies,
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.10.3"),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1")
  )

lazy val httpDependencies = Seq(
  "org.http4s"            %% "http4s-blaze-server" % Http4sVersion,
  "org.http4s"            %% "http4s-blaze-client" % Http4sVersion,
  "org.http4s"            %% "http4s-circe"        % Http4sVersion,
  "org.http4s"            %% "http4s-dsl"          % Http4sVersion,
)

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature",
  "-Xfatal-warnings"
)

onChangedBuildSource in Global := ReloadOnSourceChanges

run / fork := true
outputStrategy := Some(StdoutOutput)
