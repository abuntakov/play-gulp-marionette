name := "play-core"

organization := "org.sample"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "org.scalikejdbc" %% "scalikejdbc" % "2.2.8",
  "org.scalikejdbc" %% "scalikejdbc-config" % "2.2.8",
  "org.scalikejdbc" %% "scalikejdbc-syntax-support-macro" % "2.2.8",
  "joda-time" % "joda-time" % "2.7",
  "org.postgresql" % "postgresql" % "9.4-1200-jdbc41",
  "org.postgis" % "postgis-jdbc" % "1.3.3",
  "ch.qos.logback" % "logback-classic" % "1.1.3")

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "org.scalikejdbc" %% "scalikejdbc-test" % "2.2.8" % "test")

seq(flywaySettings: _*)

flywayUrl := "jdbc:postgresql://localhost:5433/play-test"

flywayUser := "postgres"

flywayPassword := "secret"

flywayLocations := Seq( "db/migration/default" )
