package com.github.jw3.artifactory

import sbt.Keys.{publishArtifact, _}
import sbt._

import scala.language.postfixOps

case class ArtifactoryConfigException(msg: String) extends RuntimeException(msg)

trait ArtifactoryKeys {
  lazy val publishArtifacts = taskKey[Unit]("publish files to artifactory.")
  lazy val hostname = settingKey[String]("hostname of artifactory")
  lazy val username = settingKey[Option[String]]("username for artifactory")
  lazy val password = settingKey[Option[String]]("password for artifactory")
}

object ArtifactoryPlugin extends AutoPlugin {
  object autoImport extends ArtifactoryKeys {
    lazy val Artifactory: Configuration = config("artifactory")
  }

  import autoImport._


  //  override def requires = IvyPlugin

  override def projectConfigurations: Seq[Configuration] = Seq(Artifactory)

  override lazy val projectSettings: Seq[Setting[_]] = Seq(
    publishArtifact := true,
    publishMavenStyle := true,
    publishTo := {
      if (!validHostname(host.value)) None
      else {
        val targetRepo = if (isSnapshot.value) "snapshot" else "release"
        Some("Artifactory Realm" at s"https://${host.value}/libs-$targetRepo-local")
      }
    },
    credentials := {
      if (user.value.isDefined) Seq(
        Credentials("Artifactory Realm", host.value, user.value.get, pass.value.getOrElse(""))
      )
      else Seq.empty
    }

  ) ++ inConfig(Artifactory)(
    Seq(
      publishArtifacts := {
        if (publishTo.value.isEmpty) sys.error("Artifactory hostname was not specified.")

        val log = streams.value.log
        log.info(s"============== Publish ${project.value} to Artifactory at ${host.value}${user.value.map(u ⇒ s" as $u").getOrElse("")} ==============")

        publish.value
      },
      hostname := {
        sys.env.getOrElse("ARTIFACTORY_DEPLOY_HOST", "")
      },
      username := {
        sys.env.get("ARTIFACTORY_DEPLOY_USER")
      },
      password := {
        sys.env.get("ARTIFACTORY_DEPLOY_PASS").orElse(
          sys.env.get("ARTIFACTORY_DEPLOY_TOKEN")
        )
      }
    )
  )

  private def host = hostname in Artifactory
  private def user = username in Artifactory
  private def pass = password in Artifactory
  private def project = name in ThisProject

  private def validHostname(uri: String): Boolean = uri.nonEmpty
}
