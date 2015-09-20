package ohnosequences.cosas.tests

import ohnosequences.cosas._, fns._

object sampleFunctions {

  case object size extends DepFn1[Any,Int] {

    implicit val sizeForStr   = this at { x: String => x.length }
    implicit val sizeForChar  = this at { x: Char   => 1 }
    implicit val sizeForInt   = this at { x: Int    => x }
  }

  case object print extends DepFn1[Any,String] {

    implicit val atInt = this at { n: Int => s"${n}: Int" }
    implicit val atString = this at { str: String => s"""'${str}': String""" }
  }

  import typeSets._

  // alternative mapping over typeSets
  case object MapToHList extends DepFn2[AnyDepFn1, AnyTypeSet, shapeless.HList] {

    import shapeless._

    implicit def empty[F <: AnyDepFn1] = this at { (f: F, e: ∅) => HNil }

    implicit def cons[
      F <: AnyDepFn1,
      H <: F#In1, T <: AnyTypeSet,
      OutH, OutT <: HList
    ](implicit
      evF: App1[F,H] { type Out = OutH },
      evThis: App2[this.type,F,T] { type Out = OutT }
    )
    : App2[this.type, F,H :~: T] { type Out = OutH :: OutT } =
      this at { (f: F, ht: H :~: T) => f(ht.head) :: this(f,ht.tail) }
  }

}

class DependentFunctionsTests extends org.scalatest.FunSuite {

  import sampleFunctions._
  import shapeless._
  import typeSets._

  test("can apply dependent functions") {

    assert { 2 === size(size(2)) }
    assert { size(4) === size("four") }

    val b = "lala" :~: 'a' :~: 2 :~: ∅

    assert { 4 :: 1 :: 2 :: HNil === MapToHList(size,b) }
  }

  test("can compose and apply dependent functions") {

    assert { "1: Int" === Composition(size,print)(1) }
    assert { Composition(size,print)(1) === Composition(size,print)(1) }
  }
}
