package ohnosequences.cosas.tests

import ohnosequences.cosas._, fns._
import typeSets._
import sampleFunctions._
import shapeless.{union => _, DepFn1 => _, DepFn2 => _, _ }

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

    val uh = print(2)
    assert { 2 === size(size("bu")) }
    assert { size(4) === size("four") }

    val a = "ohoho" :~: 'c' :~: ∅
    val b = "lala" :~: 'a' :~: 2 :~: ∅
    val c = "lololo" :~: true :~: ∅

    assert { ("ohoho", 'c' :~: ∅) === (new pop[String])(a) }
    assert { (true, "lololo" :~: ∅) === pop[Boolean](c) }

    // val zzz = mapToListOf[Int].apply(size,b)

    // depFn2ApplySyntax[mapToHList, size.type, Int :~: ∅, Int :: HNil](MapToHList).apply(size,2 :~: ∅)

    // assert { 4 :: 1 :: 2 :: HNil === MapToHList(size,b) }

    val ab = union(union(a,b),a)
    val ba = union(b,a)
    val bc = union(b,c)
    val cb = union(c,b)
    val abc = union(union(a,b),c)
  }
}
