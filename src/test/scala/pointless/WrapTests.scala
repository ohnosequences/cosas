package ohnosequences.pointless.tests

import shapeless.test.illTyped
import ohnosequences.pointless._, AnyWrap._, ValueOf._

object WrapTestsContext {

  case object Color extends Wrap[String]
}

class WrapTests extends org.scalatest.FunSuite {
  import WrapTestsContext._

  test("creating values") {

    val azul = Color("blue")
    val verde = valueOf[Color.type]("green")
    val amarillo = Color withValue "yellow"

    assert{ azul.raw == "blue" }
    assert{ verde.raw == "green" }
    assert{ amarillo.raw == "yellow" }
  }
}
