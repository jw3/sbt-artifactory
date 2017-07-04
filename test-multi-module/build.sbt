lazy val commonSettings = Seq(
  organization := "com.github.jw3",
  version := "0.3-SNAPSHOT",
  scalaVersion := "2.12.2"
)

lazy val `test-multi-module` =
  project.in(file("."))
  .aggregate(a, b, c)
  .settings(
    name := "sbt-artifactory-test-multi"
  )
  .settings(commonSettings)

lazy val a =
  project.in(file("a"))
  .settings(
    name := "sbt-artifactory-test-multi-a"
  )
  .settings(commonSettings)
  .enablePlugins(ArtifactoryPlugin)

lazy val b =
  project.in(file("b"))
  .settings(
    name := "sbt-artifactory-test-multi-b"
  )
  .settings(commonSettings)

lazy val c =
  project.in(file("c"))
  .settings(
    name := "sbt-artifactory-test-multi-c"
  )
  .settings(commonSettings)
  .enablePlugins(ArtifactoryPlugin)
