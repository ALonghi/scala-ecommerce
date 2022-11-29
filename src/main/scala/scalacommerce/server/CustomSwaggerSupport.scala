package scalacommerce.server

import cats.effect.Sync
import cats.implicits.catsSyntaxOptionId
import com.http4s.rho.swagger.ui.SwaggerUiRoutes
import org.http4s.rho.RhoMiddleware
import org.http4s.rho.bits.PathAST.{ PathAnd, PathMatch, TypedPath }
import org.http4s.rho.swagger.{ DefaultSwaggerFormats, SwaggerMetadata, SwaggerSupport, SwaggerSyntax }
import org.http4s.rho.swagger.models._
import shapeless._

import scala.reflect.runtime.universe._

object CustomSwaggerSupport {
  def apply[F[_]](implicit F: Sync[F], etag: WeakTypeTag[F[_]]): CustomSwaggerSupport[F] =
    new CustomSwaggerSupport[F] {}
}

abstract class CustomSwaggerSupport[F[_]](implicit F: Sync[F], etag: WeakTypeTag[F[_]]) extends SwaggerSyntax[F] {

  def createRhoMiddleware(): (RhoMiddleware[F], RhoMiddleware[F]) =
    ({ routes => routes }, { routes =>
      lazy val swagger: Swagger = SwaggerSupport[F].createSwagger(
        swaggerFormats = DefaultSwaggerFormats,
        SwaggerMetadata(
          apiInfo          = Info(
            title   = "API",
            version = "1.0.0"
          ),
          host             = None,
          basePath         = "/".some,
          schemes          = List(Scheme.HTTPS, Scheme.HTTP),
          consumes         = Nil,
          produces         = Nil,
          tags             = Nil,
          vendorExtensions = Map.empty
        )
      )(routes)

      val swaggerJsonPath = TypedPath[F, HNil](PathAnd(PathMatch("api"), PathMatch("swagger.json")))
      val swaggerYamlPath = TypedPath[F, HNil](PathAnd(PathMatch("api"), PathMatch("swagger.yaml")))
      val swaggerSpecRoute = SwaggerSupport[F].createSwaggerRoute(swagger, swaggerJsonPath, swaggerYamlPath).getRoutes

      val swaggerUiRoutes = SwaggerUiRoutes[F]("api", "swagger.json").getRoutes

      swaggerSpecRoute ++ swaggerUiRoutes
    })
}
