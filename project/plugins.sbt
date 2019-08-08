addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "1.0.0")
addSbtPlugin("org.scalameta"  % "sbt-scalafmt"           % "2.0.2")
addSbtPlugin("org.scoverage"  % "sbt-scoverage"          % "1.6.0")
addSbtPlugin("com.typesafe.sbt"   % "sbt-native-packager"      % "1.3.25")
addSbtPlugin("com.eed3si9n"     % "sbt-buildinfo"       % "0.9.0")
addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "0.6.1")
addSbtPlugin("com.typesafe.sbt"   % "sbt-multi-jvm"            % "0.4.0")
addSbtPlugin("com.timushev.sbt"   % "sbt-updates"              % "0.4.2")
addSbtPlugin("com.typesafe.sbt" % "sbt-twirl"    % "1.4.0")
addSbtPlugin("io.spray"         % "sbt-revolver" % "0.9.1")

// web client
addSbtPlugin("org.scala-js"  % "sbt-scalajs"             % "0.6.28")
addSbtPlugin("com.vmunier"   % "sbt-web-scalajs"         % "1.0.8-0.6")
addSbtPlugin("ch.epfl.scala" % "sbt-web-scalajs-bundler" % "0.15.0-0.6")

addSbtCoursier
classpathTypes += "maven-plugin"
