sbtPlugin := true

organization := "com.github.jw3"
name := "sbt-artifactory"
version := "0.3-SNAPSHOT"

scalaVersion := "2.10.5"
scalacOptions in Compile ++= Seq("-deprecation", "-target:jvm-1.7")

publishMavenStyle := false

bintrayRepository := {
  if (isSnapshot.value) "sbt-plugin-snapshots" else "sbt-plugin-releases"
}

enablePlugins(BintrayPlugin)
