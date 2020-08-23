name := "untitled1"

version := "0.1"

scalaVersion := "2.11.8"

val akkaV       = "2.4.3"
val scalaTestV  = "2.2.6"

libraryDependencies ++= {Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaV,
  "com.typesafe.akka" %% "akka-stream" % akkaV,
  "com.typesafe.akka" %% "akka-http-experimental" % akkaV,
  "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaV,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaV,
  "com.github.etaty" %% "rediscala" % "1.8.0",
  "org.scalatest" %% "scalatest" % scalaTestV % "test",
  "org.scalamock" %% "scalamock-scalatest-support" % "3.4.2",

)
}