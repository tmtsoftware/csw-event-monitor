import sbt.Keys.{libraryDependencies, resolvers}
import scalajsbundler.sbtplugin.ScalaJSBundlerPlugin.autoImport.npmDependencies

lazy val root = (project in file("."))
  .aggregate(`csw-event-monitor-server`, `csw-event-monitor-client`, `test-hcd`)
  .settings(name := "csw-event-monitor")

lazy val `csw-event-monitor-server` = project
  .enablePlugins(DeployApp, SbtWeb, SbtTwirl, WebScalaJSBundlerPlugin)
  .settings(
    scalaJSProjects := Seq(`csw-event-monitor-client`),
    pipelineStages in Assets := Seq(scalaJSPipeline),
    // triggers scalaJSPipeline when using compile or continuous compilation
    compile in Compile := ((compile in Compile) dependsOn scalaJSPipeline).value,
    libraryDependencies ++= Seq(
      Akka.`akka-http`,
      Akka.`akka-stream`,
      Akka.`akka-http-play-json`,
      Csw.`csw-event-client`,
      Csw.`csw-location-client`,
      Libs.`akka-http-cors`,
      Libs.`scalajs-scripts`
    ),
    WebKeys.packagePrefix in Assets := "public/",
    managedClasspath in Runtime += (packageBin in Assets).value,
  )

lazy val `csw-event-monitor-client` = project
  .enablePlugins(ScalaJSBundlerPlugin, ScalaJSWeb)
  .settings(
    scalaJSUseMainModuleInitializer := true,
    resolvers += Resolver.sonatypeRepo("snapshots"),
    npmDependencies in Compile ++= Seq(
      "materialize-css"          -> "1.0.0",
      "react"                    -> "16.4.1",
      "react-dom"                -> "16.4.2",
      "chart.js"                 -> "2.7.3"
    ),
    npmDevDependencies in Compile ++= Seq(
      "webpack-merge"  -> "4.2.1",
      "imports-loader" -> "0.8.0",
      "expose-loader"  -> "0.7.5",
      "css-loader"  -> "2.1.1",
      "style-loader"  -> "0.23.1"
    ),
    scalacOptions += "-P:scalajs:sjsDefinedByDefault",
    libraryDependencies ++= Seq(
      React4s.`react4s`.value,
      Utils.`scalajs-dom`.value,
      Utils.`play-json`.value,
      Utils.`enumeratum`.value,
      Utils.`enumeratum-play-json`.value,
      Utils.`upickle`.value,
      Csw.`csw-params`.value
    ),
    version in webpack := "4.8.1",
    version in startWebpackDevServer := "3.1.4",
    webpackDevServerExtraArgs in fastOptJS ++= Seq(
      "--content-base",
      baseDirectory.value.getAbsolutePath
    )
  )

lazy val `test-hcd` = project
  .enablePlugins(DeployApp)
  .settings(
    libraryDependencies ++= Seq(
      Csw.`csw-framework`,
      Csw.`csw-prefix`,
    )
  )


//// loads the server project at sbt startup
//onLoad in Global := (onLoad in Global).value andThen { s: State =>
//  "project csw-event-monitor-server" :: s
//}
