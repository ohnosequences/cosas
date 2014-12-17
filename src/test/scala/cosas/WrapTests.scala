package ohnosequences.cosas.tests

import ohnosequences.cosas._, AnyType._

object WrapTestsContext {

  object Color extends Wrap[String]("Color")

  object UserType extends Type("User")
  type User = UserType.type
  val User: User = UserType

  object Friend extends Type("Friend")
  type Friend = Friend.type

  case class user(id: String, name: String, age: Int)

}

class WrapTests extends org.scalatest.FunSuite {
  import WrapTestsContext._

  test("creating values") {

    val azul = Color denoteWith "blue"
    val verde = new ValueOf[Color.type]("green")
    val amarillo = Color denoteWith "yellow"

    assert{ azul.value == "blue" }
    assert{ verde.value == "green" }
    assert{ amarillo.value == "yellow" }
  }
}

class DenotationTests extends org.scalatest.FunSuite {
  import WrapTestsContext._

  test("create denotations") {
    import Denotes._

    /* the right-associative syntax */
    val uh: user :%: User = user(id = "adqwr32141", name = "Salustiano", age = 143) :%: User
    val z = User denoteWith 2423423
  }

  test("type-safe equals") {
    // TODO: right imports here
    import org.scalactic._, TypeCheckedTripleEquals._
    // import org.scalactic._, ConversionCheckedTripleEquals._
    import Denotes._

    val paco = "Paco"
    val u1 = paco :%: User
    val u1Again = paco :%: User
    val u2 = paco :%: Friend

    implicitly[Equivalence[Denotes[String, User.type]]]

    assert { u1 === u1 }

    assert { u1 === u1Again }

    assert { u1 === u2 }
    // assertTypeError("u1 === u2")
  }
}
