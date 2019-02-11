import sbt._
import Def.{setting => dep}
import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._

object Libs {
  val ScalaVersion = "2.12.8"

  val `akka-http-cors`  = "ch.megard"   %% "akka-http-cors"  % "0.3.1"
  val `scalajs-scripts` = "com.vmunier" %% "scalajs-scripts" % "1.1.2"
}

object Csw {
  private val Org     = "com.github.tmtsoftware.csw"
  private val Version = "0.1-SNAPSHOT"

  val `csw-location-client` = Org %% "csw-location-client" % Version
  val `csw-event-client`    = Org %% "csw-event-client" % Version
  val `csw-params`          = dep(Org %%% "csw-params" % Version)
}

object Akka {
  val Version = "2.5.19"

  val `akka-actor`          = "com.typesafe.akka" %% "akka-actor"               % Version
  val `akka-stream`         = "com.typesafe.akka" %% "akka-stream"              % Version
  val `akka-typed`          = "com.typesafe.akka" %% "akka-actor-typed"         % Version
  val `akka-http`           = "com.typesafe.akka" %% "akka-http"                % "10.1.5"
  val `akka-typed-testkit`  = "com.typesafe.akka" %% "akka-actor-testkit-typed" % Version
  val `akka-http-play-json` = "de.heikoseeberger" %% "akka-http-play-json"      % "1.21.0" //Apache 2.0
}

object React4s {
  val `react4s`       = dep("com.github.ahnfelt" %%% "react4s"       % "0.9.24-SNAPSHOT")
  val `router4s`      = dep("com.github.werk"    %%% "router4s"      % "0.1.0-SNAPSHOT")
  val `jquery-facade` = dep("org.querki"         %%% "jquery-facade" % "1.2")
}

object Utils {
  val `play-json`            = dep("com.typesafe.play" %%% "play-json" % "2.6.10") //Apache 2.0
  val `enumeratum`           = dep("com.beachape" %%% "enumeratum" % "1.5.13")
  val `enumeratum-play-json` = dep("com.beachape" %%% "enumeratum-play-json" % "1.5.14")
  val `paths-scala-js`       = dep("eu.unicredit" %%% "paths-scala-js" % "0.4.5")
}
