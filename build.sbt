import sbt.Keys.{libraryDependencies, resolvers}
import scalajsbundler.sbtplugin.ScalaJSBundlerPlugin.autoImport.npmDependencies

lazy val `csw-event-monitor-server` = project
  .enablePlugins(DeployApp)
  .settings(
    libraryDependencies ++= Seq(
      Akka.`akka-http`,
      Akka.`akka-stream`,
      Csw.`csw-event-client`,
      Csw.`csw-location-client`,
      Libs.`akka-http-cors`
    )
  )

lazy val `csw-event-monitor-client` = project
  .enablePlugins(ScalaJSBundlerPlugin)
  .settings(
    scalaJSUseMainModuleInitializer := true,
    resolvers += Resolver.sonatypeRepo("snapshots"),
    npmDependencies in Compile ++= Seq(
      "react" -> "16.4.1",
      "react-dom" -> "16.4.2"
    ),
    scalacOptions += "-P:scalajs:sjsDefinedByDefault",
    libraryDependencies ++= Seq(
      React4s.`react4s`.value,
      React4s.`router4s`.value,
      Utils.`play-json`.value,
      Utils.`enumeratum`.value,
      Utils.`enumeratum-play-json`.value,
      Csw.`csw-params`.value,
  ),
    version in webpack := "4.8.1",
    version in startWebpackDevServer := "3.1.4",
    webpackResources := webpackResources.value +++
      PathFinder(Seq(baseDirectory.value / "index.html")) ** "*.*",
    webpackDevServerExtraArgs in fastOptJS ++= Seq(
      "--content-base",
      baseDirectory.value.getAbsolutePath
    )
  )
