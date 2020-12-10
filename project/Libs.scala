import sbt._
import Def.{setting => dep}
import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._

object Libs {
  val ScalaVersion = "2.13.4"

  val `akka-http-cors` = "ch.megard" %% "akka-http-cors" % "0.4.3"
  val `scalajs-scripts` = "com.vmunier" %% "scalajs-scripts" % "1.1.4"
}

object Csw {
  private val Org = "com.github.tmtsoftware.csw"
  private val Version = "0.1.0-SNAPSHOT"
//    private val Version = "v3.0.0-RC2"

  val `csw-location-client` = Org %% "csw-location-client" % Version
  val `csw-event-client` = Org %% "csw-event-client" % Version
  val `csw-params` = dep(Org %%% "csw-params" % Version)
  val `csw-framework` = Org %% "csw-framework" % Version
  val `csw-prefix` = Org %% "csw-prefix" % Version
}

object Akka {
  val Version = "2.6.10"

  val `akka-actor` = "com.typesafe.akka" %% "akka-actor" % Version
  val `akka-stream` = "com.typesafe.akka" %% "akka-stream" % Version
  val `akka-typed` = "com.typesafe.akka" %% "akka-actor-typed" % Version
  val `akka-typed-testkit` = "com.typesafe.akka" %% "akka-actor-testkit-typed" % Version

  val `akka-http` = "com.typesafe.akka" %% "akka-http" % "10.2.2"
  val `akka-http-play-json` = "de.heikoseeberger" %% "akka-http-play-json" % "1.35.2" //Apache 2.0
}

object React4s {
  val `react4s` = dep("com.github.ahnfelt" %%% "react4s" % "0.9.29-SNAPSHOT")
  val `jquery-facade` = dep("org.querki" %%% "jquery-facade" % "1.2")
}

object Utils {
  val `play-json` = dep("com.typesafe.play" %%% "play-json" % "2.8.1") //Apache 2.0
  val `enumeratum` = dep("com.beachape" %%% "enumeratum" % "1.5.15") //MIT License
  val `enumeratum-play-json` = dep("com.beachape" %%% "enumeratum-play-json" % "1.5.17") //MIT License
  val `upickle` = dep("com.lihaoyi" %%% "upickle" % "0.9.9")
  val `scalajs-dom` = dep("org.scala-js" %%% "scalajs-dom" % "0.9.8")
}
