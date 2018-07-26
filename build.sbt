import Dependencies._

lazy val `fpa-course` = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "fpa",
      scalaVersion := "2.12.6",
      version      := "0.0.1"
    )),
    name := "fpa-course",
    libraryDependencies += scalaTest % Test
  )
