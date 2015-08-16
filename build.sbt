Nice.scalaProject

name          := "cosas"
organization  := "ohnosequences"
description   := "esas cosas raras con muchos tipos"

bucketSuffix := "era7.com"

scalaVersion        := "2.11.7"
crossScalaVersions  := Seq("2.10.5")

libraryDependencies += {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, scalaMajor)) if scalaMajor >= 11 =>
              "com.chuusai" %% "shapeless" % "2.0.0"
    case _ => "com.chuusai"  % "shapeless" % "2.0.0" cross CrossVersion.full
  }
}

libraryDependencies ++= Seq (
  "org.scalatest"  %% "scalatest" % "2.2.5" % Test
)

// shows time for each test:
testOptions in Test += Tests.Argument("-oD")
