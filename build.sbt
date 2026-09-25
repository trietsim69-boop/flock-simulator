ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.5"

lazy val root = (project in file("."))
  .settings(
    name := "Flock Simulator"
  )
libraryDependencies += "org.scalafx" % "scalafx_3" % "21.0.0-R32"
Compile / mainClass := Some("Launcher")
fork := true