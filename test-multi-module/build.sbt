
organization := "com.github.jw3"
name := "sbt-artifactory-test-multi"
version := "0.2-SNAPSHOT"

scalaVersion := "2.12.2"

lazy val `test-multi-module` =
  project.in(file("."))
  .aggregate(a, b)

lazy val a =
  project.in(file("a"))
  .enablePlugins(ArtifactoryPlugin)

lazy val b =
  project.in(file("b"))
