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
parallelExecution in Test := false

// scoverage conf
// coverageEnabled := true
coverageMinimum := 90.0
coverageFailOnMinimum := true
coverageHighlighting := true
// ambiguous implicit trick => untestable
coverageExcludedPackages := "ohnosequences.cosas.Distinct;ohnosequences.cosas.NotSubtypeOf"

// for debugging
// scalacOptions ++= Seq("-Xlog-implicits")
incOptions := incOptions.value.withNameHashing(false)
wartremoverExcluded ++= Seq(
  baseDirectory.value / "src" / "main" / "scala" / "cosas" / "subtyping.scala",
  baseDirectory.value / "src" / "main" / "scala" / "cosas" / "elgot"/"elgot.scala"
)

bucketSuffix  := "era7.com"
