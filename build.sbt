Nice.scalaProject

name := "type-sets"

description := "type-sets project"

organization := "ohnosequences"

bucketSuffix := "era7.com"

libraryDependencies ++= Seq (
    "com.chuusai" %% "shapeless" % "1.2.4"
  , "org.scalatest" %% "scalatest" % "2.0" % "test"
  )
