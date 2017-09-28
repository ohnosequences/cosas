name          := "cosas"
organization  := "ohnosequences"
description   := "esas cosas raras con muchos tipos"

crossScalaVersions := Seq("2.11.11", "2.12.3")
scalaVersion  := crossScalaVersions.value.last

bucketSuffix  := "era7.com"

libraryDependencies ++= Seq (
  "org.scalatest"  %% "scalatest" % "3.0.4" % Test
)

// shows time for each test:
testOptions in Test += Tests.Argument("-oD")

// scoverage conf
// coverageEnabled := true
coverageMinimum := 90.0
coverageFailOnMinimum := true
coverageHighlighting := true
// ambiguous implicit trick => untestable
coverageExcludedPackages := "ohnosequences.cosas.Distinct;ohnosequences.cosas.NotSubtypeOf"

// for debugging
// scalacOptions ++= Seq("-Xlog-implicits")
// incOptions := incOptions.value.withNameHashing(false)

wartremoverExcluded ++= Seq(
  baseDirectory.value / "src" / "main" / "scala" / "cosas" / "subtyping.scala"
)

wartremoverErrors in (Test, compile) --= Seq(
  Wart.Product,
  Wart.Serializable
)
