name := "ad-realtime-bidder"

version := "0.1"

scalaVersion := "2.13.6"

val AkkaVersion          = "2.6.16"
val AkkaHttpVersion      = "10.2.4"
val AkkaHttpCirceVersion = "1.36.0"
val CirceVersion         = "0.14.1"
val LogbackVersion       = "1.2.3"
val ScalaTestVersion     = "3.2.9"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream-typed"   % AkkaVersion,
  "com.typesafe.akka" %% "akka-http"           % AkkaHttpVersion,
  // JSON serialization
  "de.heikoseeberger" %% "akka-http-circe"     % AkkaHttpCirceVersion,
  "io.circe"          %% "circe-core"          % CirceVersion,
  "io.circe"          %% "circe-generic"       % CirceVersion,
  "io.circe"          %% "circe-parser"        % CirceVersion,
  // Logging
  "ch.qos.logback"     % "logback-classic"     % LogbackVersion,
  // Test
  "com.typesafe.akka" %% "akka-stream-testkit" % AkkaVersion      % Test,
  "com.typesafe.akka" %% "akka-http-testkit"   % AkkaHttpVersion  % Test,
  "org.scalatest"     %% "scalatest"           % ScalaTestVersion % Test
)
