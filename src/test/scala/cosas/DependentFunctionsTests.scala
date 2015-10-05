package ohnosequences.cosas.tests

import ohnosequences.cosas._, fns._
import sampleFunctions._

object sampleFunctions {

  case object size extends DepFn1[Any,Int] {

    implicit val sizeForInt: App1[this.type,Int,Int]   = this at { x: Int    => x }
    implicit val sizeForStr: App1[this.type,String,Int] = this at { _.length }
    implicit val sizeForChar: App1[this.type,Char,Int]  = this at { x: Char   => 1 }
  }

  case object print extends DepFn1[Any,String] {

    implicit val atInt    : App1[print.type,Int,String]     = print at { n: Int => s"${n}: Int" }
    implicit val atString : App1[print.type,String,String]  = print at { str: String => s"""'${str}': String""" }
  }
}

class DependentFunctionsTests extends org.scalatest.FunSuite {

  test("can apply dependent functions") {

    assert { 2 === size(size("bu")) }
    assert { size(4) === size("four") }
  }
}
