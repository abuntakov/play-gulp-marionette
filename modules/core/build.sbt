name := "play-core"

organization := "org.sample"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "org.scalikejdbc" %% "scalikejdbc" % "2.2.7",
  "org.scalikejdbc" %% "scalikejdbc-config" % "2.2.7",
  "joda-time" % "joda-time" % "2.7",
  "org.postgresql" % "postgresql" % "9.4-1200-jdbc41",
  "org.postgis" % "postgis-jdbc" % "1.3.3",
  "ch.qos.logback" % "logback-classic" % "1.1.3")

seq(flywaySettings: _*)

flywayUrl := "jdbc:postgresql://localhost:5432/play"

flywayUser := "postgres"

flywayPassword := "secret"

flywayLocations := Seq( "db/migration/default" )
