name := "play-core"

organization := "org.sample"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.scalikejdbc" %% "scalikejdbc" % "2.3.5",
  "org.scalikejdbc" %% "scalikejdbc-config" % "2.3.5",
  "org.scalikejdbc" %% "scalikejdbc-syntax-support-macro" % "2.3.5",
  "joda-time" % "joda-time" % "2.7",
  "org.postgresql" % "postgresql" % "9.4-1200-jdbc41",
  "org.postgis" % "postgis-jdbc" % "1.3.3",
  "ch.qos.logback" % "logback-classic" % "1.1.6")

libraryDependencies += "com.typesafe.play" %% "play-json" % "2.5.0"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "org.scalikejdbc" %% "scalikejdbc-test" % "2.3.5" % "test")

libraryDependencies ~= { _.map(_.exclude("org.slf4j", "slf4j-simple")) }

Seq(flywaySettings: _*)

flywayUrl := "jdbc:postgresql://localhost:5433/play-test"

flywayUser := "postgres"

flywayPassword := "secret"

flywayLocations := Seq( "db/migration/default" )
