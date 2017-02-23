name := "hlist"

version := "1.0"

scalaVersion := "2.12.1"

libraryDependencies += "com.chuusai" %% "shapeless" % "2.3.2"

lazy val akkaVersion = "2.4.16"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % "2.4.17",
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.4.17",
  "org.reactivemongo" %% "reactivemongo" % "0.12.1",
  "org.reactivemongo" %% "reactivemongo-akkastream" % "0.12.1"
)

libraryDependencies += "org.slf4j" % "slf4j-log4j12" % "1.7.10"
libraryDependencies += "com.typesafe.akka" %% "akka-stream-kafka" % "0.13"