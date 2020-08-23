
//------------------------------ Zeta User App Demo ------------------------------------

// Importing necessary Libraries for support Version Number make a lot of struggles

import akka.actor.{ActorRef, ActorSystem}
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes.{InternalServerError, NoContent, OK}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.Credentials
import akka.pattern.ask
import akka.stream.{ActorMaterializer, Materializer}
import akka.util.Timeout
import com.typesafe.config.{Config, ConfigFactory}
import redis.RedisClient
import Repository.{ConcreteRedis, RedisRepoImp}
//import ServerDemo.{pwd, redisUrl}
import spray.json.DefaultJsonProtocol

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.io.StdIn

// Defining case classes for Zeta Food App

final case class Zeta(id: String,name: String , amt: Int)
final case class userId(id: String)
final case class updUser(id: String,amt: Int)


trait SuppFormat extends DefaultJsonProtocol{
  implicit val zusr = jsonFormat3(Zeta)
  implicit val uid = jsonFormat1(userId)
  implicit val updusr = jsonFormat2(updUser)
}




object ServerDemo  extends App with ConcreteRedis with SuppFormat
{

   implicit val system = ActorSystem("my-zeta-app")
   implicit val executor = system.dispatcher
   implicit val materializer = ActorMaterializer()

   // Redis DataBase Elastic Search
  def db = RedisClient(host = redisUrl.getHost, port = redisUrl.getPort, password = pwd)
  var MAmt :Map[String,Int] = Map()
  var MName : Map[String,String] = Map()
  // Routes for CURD operation

   val route:Route  = pathPrefix("zeta") {
     get {
       path("home") {
         complete(" Welcome to Zeta App where you can order food !!! ")
       }
     }~
       get{
         path("getUser") {
           entity(as[userId]) { uid =>
             println(db.get(uid.id))
             complete(s"User name is ${MName(uid.id)} and available amount is  is ${MAmt(uid.id)}")
           }
         }
       }~
     put {
       path("create-user") {
         entity(as[Zeta]) { usr =>
           db.set(usr.id,usr.name)
           Thread.sleep(500)
           println(db.get(usr.id))

           MAmt += (usr.id -> usr.amt)
           MName += (usr.id -> usr.name)
           complete(s"New User Created! with name ${usr.name}")
         }
       }
     }~
     delete{
       path("del-user")
       {
         entity(as[userId]){ uid =>
           db.del(uid.id)
           MAmt.-(uid.id)
           MName.-(uid.id)
           complete(s"User deleted with User Id  ${uid.id}")
         }
       }
     }~
     post{
       path("update-usr")
       {
           entity(as[updUser]) { usrdetail =>
             db.set(usrdetail.id,usrdetail.amt)
             MAmt += (usrdetail.id -> usrdetail.amt)
           complete("User Details Updated")
         }
       }
     }

   }




  val bindingFuture = Http().bindAndHandle(route ,"0.0.0.0", 8080)

  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when done

  println("Installed")
}
