name := "ZIO101"

version := "0.1"

scalaVersion := "2.12.8"

scalacOptions := Seq(
  "-feature",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-unchecked",
  "-deprecation",
  "-Xfatal-warnings"
)

//ZIO
libraryDependencies += "dev.zio" %% "zio" % "1.0.1"

//Test
libraryDependencies ++= Seq(
  "dev.zio" %% "zio-test"          % "1.0.1" % "test",
  "dev.zio" %% "zio-test-sbt"      % "1.0.1" % "test",
  "dev.zio" %% "zio-test-magnolia" % "1.0.1" % "test" // optional
)
testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")

// Cats Integration
libraryDependencies += "dev.zio" %% "zio-interop-cats" % "2.1.4.0"

// HTTP4S
libraryDependencies += "org.http4s" %% "http4s-blaze-server" % "1.0.0-M4"
libraryDependencies += "org.http4s" %% "http4s-circe" % "1.0.0-M4"
libraryDependencies += "org.http4s" %% "http4s-dsl" % "1.0.0-M4"

// DOOBIE
libraryDependencies +=  "org.tpolecat" %% "doobie-core" % "0.9.0"
libraryDependencies +=  "org.tpolecat" %% "doobie-h2" % "0.9.0"

// Config
libraryDependencies += "com.github.pureconfig" %% "pureconfig" % "0.13.0"

// Tracing
libraryDependencies += "dev.zio" %% "zio-opentracing" % "0.7.0"

// Streams
libraryDependencies ++= Seq(
  "dev.zio" %% "zio-streams" % "1.0.0",
  "dev.zio" %% "zio-kafka"   % "0.12.0"
)

// Kittens
libraryDependencies += "org.typelevel" %% "kittens" % "2.2.0"


// Logging
libraryDependencies += "dev.zio" %% "zio-logging" % "0.5.2"
libraryDependencies += "dev.zio" %% "zio-logging-slf4j" % "0.5.2"

libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.6.1" % Runtime

libraryDependencies += "com.nequissimus" %% "circe-kafka" % "2.3.0"

val circeVersion = "0.12.3"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)