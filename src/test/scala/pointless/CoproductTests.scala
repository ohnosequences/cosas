package ohnosequences.pointless.tests

import ohnosequences.pointless._
import shapeless.{Nat, Succ}, Nat._
import shapeless.test.illTyped

object CoproductTestContext {

  object SI extends (String :+: Int :+: CNil)
}

class CoproductTests extends org.scalatest.FunSuite {

  import CoproductTestContext._

  test("create coproduct values") {

    val uh = SI at(_1, 343)
    val uhoh = SI at(_2, "lalala")

    illTyped("""

      val nnn = SI at(_2, true)
    """)

    illTyped("""

      val nnn = SI at(_1, true)
    """)
  }
}
