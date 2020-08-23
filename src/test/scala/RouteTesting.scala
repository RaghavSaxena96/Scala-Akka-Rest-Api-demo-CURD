// Testing of Routes
import java.security.Provider.Service

import akka.actor.ActorSystem
import akka.event.NoLogging
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.ContentTypes._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.headers.{BasicHttpCredentials, HttpChallenge}
import akka.http.scaladsl.server.AuthenticationFailedRejection
import akka.http.scaladsl.server.AuthenticationFailedRejection.{CredentialsMissing, CredentialsRejected}
import akka.http.scaladsl.testkit.{RouteTestTimeout, ScalatestRouteTest}
import akka.testkit.TestActorRef
import org.scalamock.scalatest.MockFactory
import org.scalatest._
import org.scalatest.concurrent.ScalaFutures
import Repository.Repo
import ServerDemo.route
import akka.http.impl.util.JavaAccessors.HttpRequest

import scala.concurrent.Future
import scala.concurrent.duration._

class RouteTesting extends FlatSpec  with Matchers  with ScalatestRouteTest  with Service  with ScalaFutures  with MockFactory
{
  implicit def default(implicit system: ActorSystem) = RouteTestTimeout(5 seconds)
  override def testConfigSource = "akka.loglevel = DEBUG"

  Get("/zeta/home") ~> route ~> check {
    status shouldBe OK
    responseAs[String] shouldBe " Welcome to Zeta App where you can order food !!! "
  }



}
