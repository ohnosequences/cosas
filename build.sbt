Nice.scalaProject

name          := "cosas"
organization  := "ohnosequences"
description   := "esas cosas raras con muchos tipos"

scalaVersion  := "2.11.7"
// will update
// crossScalaVersions  := Seq("2.12.0-M3")

libraryDependencies ++= Seq (
  "org.scalatest"  %% "scalatest" % "2.2.5" % Test
)

// shows time for each test:
testOptions in Test += Tests.Argument("-oD")

// scalacOptions ++= Seq("-Xlog-implicits")

incOptions := incOptions.value.withNameHashing(false)

wartremoverExcluded ++= Seq(
  baseDirectory.value / "src" / "main" / "scala" / "cosas" / "products" / "map.scala",
  baseDirectory.value / "src" / "main" / "scala" / "cosas" / "products" / "klists.scala",
  baseDirectory.value / "src" / "main" / "scala" / "cosas" / "subtyping.scala"
)

bucketSuffix  := "era7.com"
