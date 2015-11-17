import Gulp._
import play.PlayImport.PlayKeys.playRunHooks

playRunHooks <+= baseDirectory.map(base => Gulp(base / "project"))

lazy val commonSettings = Seq(
  organization := "org.sample",
  version := "0.0.1",
  scalaVersion := "2.11.7",
  scalacOptions ++= Seq("-feature")
  )

name := "play-web"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "org.scalikejdbc" %% "scalikejdbc-play-initializer" % "2.4.2",
  "org.flywaydb" %% "flyway-play" % "2.2.0",
  "ch.qos.logback" % "logback-classic" % "1.1.3")

lazy val root = (project in file("."))
  .settings(commonSettings:_*)
  .enablePlugins(PlayScala)
  .aggregate(core)
  .dependsOn(core)


lazy val core = (project in file("modules/core"))
  .settings(commonSettings:_*)
