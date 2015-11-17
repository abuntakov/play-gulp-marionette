name := "play-core"

organization := "org.sample"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "org.scalikejdbc" %% "scalikejdbc" % "2.2.9",
  "org.scalikejdbc" %% "scalikejdbc-config" % "2.2.9",
  "org.scalikejdbc" %% "scalikejdbc-syntax-support-macro" % "2.2.9",
  "joda-time" % "joda-time" % "2.9",
  "org.postgresql" % "postgresql" % "9.4-1200-jdbc41",
  "org.postgis" % "postgis-jdbc" % "1.3.3",
  "ch.qos.logback" % "logback-classic" % "1.1.3")

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.5" % "test",
  "org.scalikejdbc" %% "scalikejdbc-test" % "2.2.9" % "test")

libraryDependencies ~= { _.map(_.exclude("org.slf4j", "slf4j-simple")) }

Seq(flywaySettings: _*)

flywayUrl := "jdbc:postgresql://localhost:5433/play-test"

flywayUser := "postgres"

flywayPassword := "secret"

flywayLocations := Seq( "db/migration/default" )
