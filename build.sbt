import Gulp._
import play.PlayImport.PlayKeys.playRunHooks

playRunHooks <+= baseDirectory.map(base => Gulp(base / "project"))

name := "play-gulp-marionette"

version := "0.0.1"

scalaVersion := "2.11.7"

lazy val root = (project in file(".")).enablePlugins(PlayScala)
