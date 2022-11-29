import sbt._
import Keys._
import scalariform.formatter.preferences._
import com.typesafe.sbt.SbtScalariform.ScalariformKeys

//val Http4sVersion         = "0.23.12"
val Http4sVersion     = "0.23.9"
val CirceVersion      = "0.14.3"
val Log4CatsVersion   = "2.5.0"
val DoobieVersion     = "1.0.0-RC2"
val Slf4jVersion      = "2.0.4"
val PureConfigVersion = "0.17.2"
val FlywayVersion     = "9.8.3"
val RhoSwaggerVersion = "0.23.0-RC1"


lazy val formattingSettings: Seq[Def.Setting[_]] = Seq(
  ScalariformKeys.autoformat := true,
  ScalariformKeys.preferences := ScalariformKeys
    .preferences
    .value
    .setPreference(DoubleIndentConstructorArguments, true)
    .setPreference(DoubleIndentMethodDeclaration, true)
    .setPreference(AlignSingleLineCaseStatements, true)
    .setPreference(AlignParameters, true)
    .setPreference(AlignArguments, true)
    .setPreference(PreserveSpaceBeforeArguments, true)
    .setPreference(FirstArgumentOnNewline, Force)
    .setPreference(DanglingCloseParenthesis, Force)
)

lazy val root = (project in file("."))
  .enablePlugins(JavaAppPackaging)
  .enablePlugins(DockerPlugin)
  .settings(Compile / mainClass := Some("scalacommerce.server.Main"))
  .settings(dockerEntrypoint := Seq("bin/scala-ecommerce"))
  .settings(dockerEnvVars ++= Map("APP" -> "scalacommerce-app"))
  .settings(formattingSettings)
  .settings(
    organization := "scalacommerce",
    name := "scala-ecommerce",
    version := "1.0.0-SNAPSHOT",
    scalaVersion := "2.13.10",
    libraryDependencies ++= httpDependencies ++ Seq(
      "io.circe"              %% "circe-generic"   % CirceVersion,
      "io.circe"              %% "circe-parser"    % CirceVersion,
      "org.typelevel"         %% "log4cats-slf4j"  % Log4CatsVersion,
      "org.slf4j"             % "slf4j-simple"     % Slf4jVersion,
      "org.tpolecat"          %% "doobie-core"     % DoobieVersion,
      "org.tpolecat"          %% "doobie-postgres" % DoobieVersion,
      "com.github.pureconfig" %% "pureconfig"      % PureConfigVersion,
      "org.flywaydb"            % "flyway-core"           % FlywayVersion,
      "org.http4s"            %% "rho-swagger"     % RhoSwaggerVersion,
      "org.http4s"            %% "rho-swagger-ui"  % RhoSwaggerVersion
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.10.3"),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1")
  )

lazy val httpDependencies = Seq(
  "org.http4s" %% "http4s-blaze-server" % Http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % Http4sVersion,
  "org.http4s" %% "http4s-circe"        % Http4sVersion,
  "org.http4s" %% "http4s-dsl"          % Http4sVersion
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
