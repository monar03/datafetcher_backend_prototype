name := "datafetcher"

version := "1.0"

scalaVersion := "2.11.0"

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.1",
  "org.scala-lang.modules" %% "scala-xml" %  "1.0.1",
  "org.scala-lang.plugins" %% "scala-continuations-library" % "1.0.1",
  "com.typesafe.akka" %% "akka-actor" %  "2.3.7",
  "org.specs2" %% "specs2" % "3.0-M1",
  "org.twitter4j" % "twitter4j-core" % "4.0.2",
  "org.twitter4j" % "twitter4j-stream" % "4.0.2",
  "mysql" % "mysql-connector-java" % "5.1.34",
  "com.typesafe.slick" %% "slick" % "2.1.0",
  "org.slf4j" % "slf4j-nop" % "1.7.7",
  "org.jsoup" % "jsoup" % "1.8.1",
  "org.apache.commons" % "commons-lang3" % "3.3.2"
)
