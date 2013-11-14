Nice.scalaProject

name := "type-sets"

description := "type-sets project"

organization := "ohnosequences"

bucketSuffix := "era7.com"

libraryDependencies ++= Seq (
    "com.chuusai" % "shapeless_2.10.2" % "2.0.0-M1"
  , "org.scalatest" %% "scalatest" % "2.0" % "test"
  )
