package com.github.jw3.artifactory

import sbt._
import sbt.plugins.IvyPlugin

import scala.language.postfixOps

case class ArtifactoryConfigException(msg: String) extends RuntimeException(msg)

trait ArtifactoryKeys {
  lazy val publish = taskKey[Unit]("publish files to artifactory.")
  lazy val hostname = settingKey[String]("hostname of artifactory")
  lazy val username = settingKey[Option[String]]("username for artifactory")
  lazy val password = settingKey[Option[String]]("password for artifactory")
}

object ArtifactoryPlugin extends AutoPlugin {
  object autoImport extends ArtifactoryKeys {
    import Keys._

    lazy val Artifactory: Configuration = config("artifactory")

    lazy val artifactorySettings = Seq(
      hostname in Artifactory := {
        sys.env.getOrElse("ARTIFACTORY_DEPLOY_HOST", "")
      },
      username in Artifactory := {
        sys.env.get("ARTIFACTORY_DEPLOY_USER")
      },
      password in Artifactory := {
        sys.env.get("ARTIFACTORY_DEPLOY_PASS").orElse(
          sys.env.get("ARTIFACTORY_DEPLOY_TOKEN")
        )
      },
      publishTo := {
        val targetRepo = if (isSnapshot.value) "snapshot" else "release"
        Some("Artifactory Realm" at s"https://${host.value}/libs-$targetRepo-local")
      },
      publishArtifact := true,
      publishMavenStyle := true,
      credentials := {
        if (user.value.isDefined) Seq(
          Credentials("Artifactory Realm", host.value, user.value.get, pass.value.getOrElse(""))
        )
        else Seq.empty
      },
      publish in Artifactory := {
        val log = streams.value.log
        if (!validateHostname(host.value)) sys.error("Artifactory hostname was not specified.")

        log.debug(s"============== Publishing to Artifactory at ${host.value}${user.value.map(u ⇒ s" as $u").getOrElse("")} ==============")
        publish.value
      }
    )
  }

  import autoImport._

  override def requires = IvyPlugin
  override def projectConfigurations = Seq(Artifactory)
  override def projectSettings = artifactorySettings ++ inConfig(Artifactory)(artifactorySettings)

  private def host = hostname in Artifactory
  private def user = username in Artifactory
  private def pass = password in Artifactory

  private def validateHostname(uri: String): Boolean = uri.nonEmpty
}
