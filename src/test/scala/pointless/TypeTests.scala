package ohnosequences.pointless.tests

import shapeless.test.illTyped
import ohnosequences.pointless._, AnyType._, ValueOf._

object TypeTestsContext {

  case object Color extends Type[String]
}

class TypeTests extends org.scalatest.FunSuite {
  import TypeTestsContext._

  test("creating values") {

    val blue = Color("blue")
    val green = valueOf[Color.type]("green")

    assert{ blue.raw == "blue" }
    assert{ green.raw == "green" }
  }
}
