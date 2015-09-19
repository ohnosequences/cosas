package ohnosequences.cosas.tests

import ohnosequences.cosas._, fns._

object sampleFunctions {

  case object size extends DepFn1 {

    implicit val sizeForStr   = this at { x: String => x.length }
    implicit val sizeForChar  = this at { x: Char   => 1 }
    implicit val sizeForInt   = this at { x: Int    => x }
  }
}

class DependentFunctionsTests extends org.scalatest.FunSuite {

  import sampleFunctions._

  test("can apply dependent functions") {

    assert { 2 === size(size(2)) }
    assert { size(4) === size("four") }
  }
}
