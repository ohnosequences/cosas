package ohnosequences.cosas.tests

import shapeless.test.illTyped
import ohnosequences.cosas._, AnyWrap._, ValueOf._

object WrapTestsContext {

  object Color extends Wrap[String] { val label = "Color" }
}

class WrapTests extends org.scalatest.FunSuite {
  import WrapTestsContext._

  test("creating values") {

    val azul = Color("blue")
    val verde = valueOf[Color.type]("green")
    val amarillo = Color withValue "yellow"

    assert{ azul.value == "blue" }
    assert{ verde.value == "green" }
    assert{ amarillo.value == "yellow" }
  }
}

class DenotationTests extends org.scalatest.FunSuite {

  object UserType extends Type("User")

  case class user(id: String, name: String, age: Int)

  test("create denotations") {

    import Denotes._

    val uh: user Denotes UserType.type = user(id = "adqwr32141", name = "Salustiano", age = 143) :%: UserType

    val z = UserType denoteWith 2423423
  }
}
