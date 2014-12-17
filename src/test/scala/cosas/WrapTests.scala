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
  type User = UserType.type
  val User: User = UserType

  object Friend extends Type("Friend")
  type Friend = Friend.type

  case class user(id: String, name: String, age: Int)

  test("create denotations") {

    import Denotes._

    /* the right-associative syntax */
    val uh: user :%: User = user(id = "adqwr32141", name = "Salustiano", age = 143) :%: User
    val z = User denoteWith 2423423
  }

  test("type-safe equals") {

    // TODO: right imports here
    import org.scalactic.TypeCheckedTripleEquals._
    import scalaz._, Scalaz._


    val paco = "Paco"
    val u1 = paco :%: User
    val u1Again = paco :%: User
    val u2 = paco :%: Friend

    // TODO: needs integration with ScalaTest stuff.
    // Things are safe if you import the above (I don't know why)
    // illTyped {"""

    //   u1 === u2
    // """}

    assert {

      u1 === u1Again
    }

    assert {

      u1 === u1
    }


  }
}
