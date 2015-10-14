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

  test("can apply functions as dependent functions") {

    val f = { x: List[String] => x.size }

    assert { Fn1(f)(List("hola", "scalac")) === f(List("hola", "scalac")) }
  }

  test("composition?") {

    assert {
      (new Composition( size, new Composition(size,size) ))(2) === size(size(size(2)))
    }

    assert { (new Composition(new Composition(size,size), size ))(2) === size(size(size(2))) }

    assert {
      (new Composition(print,print))("abc") === print(print("abc"))
    }
  }
}
