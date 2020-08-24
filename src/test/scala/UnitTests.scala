// importing libraries

import akka.testkit._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{ Matchers, WordSpec }
import ServerDemo.route
import HttpMethods._
class UnitTests extends WordSpec with Matchers with ScalaFutures with ScalatestRouteTest {



  Get("zeta/home") ~>route ~> check {
    responseAs[String] shouldEqual "Welcome to Zeta App Enjoy your Meal !!!"
  }


}