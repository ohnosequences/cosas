Nice.scalaProject

name := "pointless"

description := "type-sets project"

organization := "ohnosequences"

bucketSuffix := "era7.com"

scalaVersion := "2.11.2"

crossScalaVersions := Seq("2.10.4", "2.11.2")

libraryDependencies += {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, scalaMajor)) if scalaMajor >= 11 =>
              "com.chuusai" %% "shapeless" % "2.0.0"
    case _ => "com.chuusai"  % "shapeless" % "2.0.0" cross CrossVersion.full
  }
}

libraryDependencies ++= Seq (
  "org.scalatest" %% "scalatest" % "2.2.1" % "test"
)
