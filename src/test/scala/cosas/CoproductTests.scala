package ohnosequences.cosas.tests

import ohnosequences.cosas._, AnyCoproduct._
import shapeless.{Nat, Succ}, Nat._
import shapeless.test.illTyped

object CoproductTestContext {

  object SI extends (String :+: Int :+: CNil)
  type SI = (String :+: Int :+: CNil)
  type S = (String :+: CNil)
}

class CoproductTests extends org.scalatest.FunSuite {

  import CoproductTestContext._

  test("create coproduct values") {

    val uh = Coproduct[SI](_1, 343)
    val uhoh = Coproduct[SI](_2, "lalala")

    // different values!
    Coproduct[String :+: Int :+: CNil](_1, 343)
    Coproduct[String :+: Int :+: Int :+: CNil](_2, 343)

    val z: ValueOfCoproduct[S] = here("hola")
    val z2: ValueOfCoproduct[SI] = here("hola")
    val z3: ValueOfCoproduct[SI] = right(here[Int,CNil](2))

    assertTypeError {
      """
      val z3: ValueOfCoproduct[SI] = right(here[String,CNil]("hola"))
      """
    }

    illTyped("""

      val nnn = SI at(_2, true)
    """)

    illTyped("""

      val nnn = SI at(_1, true)
    """)
  }
}
