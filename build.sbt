import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.12.7",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "min-triangle-path",
    libraryDependencies += "org.scala-graph" %% "graph-core" % "1.12.5",
    libraryDependencies += "com.lihaoyi" %% "fastparse" % "2.0.5",
    libraryDependencies += scalaTest % Test
  )
