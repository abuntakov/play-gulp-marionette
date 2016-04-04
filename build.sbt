import Webpack._
import play.sbt.PlayImport.PlayKeys.playRunHooks

playRunHooks += Webpack(baseDirectory.value)

// lazy val webpack = Webpack.dist(baseDirectory.value)
val webpack = taskKey[Unit]("Webpack task")

webpack := Webpack.dist(baseDirectory.value)

lazy val commonSettings = Seq(
  organization := "org.sample",
  version := "0.0.1",
  scalaVersion := "2.11.8",
  scalacOptions ++= Seq("-feature")
  )

name := "play-web"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.scalikejdbc" %% "scalikejdbc-play-initializer" % "2.5.0",
  "org.flywaydb" %% "flyway-play" % "3.0.0",
  "ch.qos.logback" % "logback-classic" % "1.1.6")

stage <<= stage dependsOn webpack

cleanFiles <+= baseDirectory { base => base / "public" }

pipelineStages := Seq(digest, gzip)


lazy val root = (project in file("."))
  .settings(commonSettings:_*)
  .enablePlugins(PlayScala)
  .aggregate(core)
  .dependsOn(core)


lazy val core = (project in file("modules/core"))
  .settings(commonSettings:_*)
