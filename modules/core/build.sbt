name := "play-core"

organization := "org.sample"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.scalikejdbc" %% "scalikejdbc" % "2.4.0",
  "org.scalikejdbc" %% "scalikejdbc-config" % "2.4.0",
  "org.scalikejdbc" %% "scalikejdbc-syntax-support-macro" % "2.4.0",
  "joda-time" % "joda-time" % "2.9.3",
  "org.postgresql" % "postgresql" % "9.4.1208",
  "org.postgis" % "postgis-jdbc" % "1.3.3",
  "ch.qos.logback" % "logback-classic" % "1.1.7")

libraryDependencies += "com.typesafe.play" %% "play-json" % "2.5.3"

libraryDependencies ++= Seq(
  "org.scalikejdbc" %% "scalikejdbc-test" % "2.4.0" % "test")

libraryDependencies ~= { _.map(_.exclude("org.slf4j", "slf4j-simple")) }

flywayUrl := "jdbc:postgresql://localhost:5433/play-test"

flywayUser := "postgres"

flywayPassword := "secret"

flywayLocations := Seq( "db/migration/default" )
