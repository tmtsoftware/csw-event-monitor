addSbtPlugin("org.scalastyle"     %% "scalastyle-sbt-plugin"   % "1.0.0")
addSbtPlugin("org.scalameta"      % "sbt-scalafmt"             % "2.3.0")
addSbtPlugin("org.scoverage"      % "sbt-scoverage"            % "1.6.1")
addSbtPlugin("com.typesafe.sbt"   % "sbt-native-packager"      % "1.5.2")
addSbtPlugin("com.eed3si9n"       % "sbt-buildinfo"            % "0.9.0")
addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "0.6.1")
addSbtPlugin("com.typesafe.sbt"   % "sbt-multi-jvm"            % "0.4.0")
addSbtPlugin("com.timushev.sbt"   % "sbt-updates"              % "0.4.2")
addSbtPlugin("com.typesafe.sbt"   % "sbt-twirl"                % "1.4.2")
addSbtPlugin("io.spray"           % "sbt-revolver"             % "0.9.1")

// web client
addSbtPlugin("org.scala-js"  % "sbt-scalajs"             % "0.6.31")
addSbtPlugin("com.vmunier"   % "sbt-web-scalajs"         % "1.0.9-0.6")
addSbtPlugin("ch.epfl.scala" % "sbt-web-scalajs-bundler" % "0.15.0-0.6")

addSbtCoursier
//classpathTypes += "maven-plugin"
