name := "dedup"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.twitter" %% "util-collection" % "6.25.0",
  "com.twitter" %% "util-app" % "6.25.0",
  "com.twitter" %% "util-logging" % "6.25.0",
  "com.github.scala-incubator.io" %% "scala-io-core" % "0.4.3",
  "com.github.scala-incubator.io" %% "scala-io-file" % "0.4.3",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test"
)
