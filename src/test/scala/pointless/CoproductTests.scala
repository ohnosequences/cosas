package ohnosequences.pointless.tests

import ohnosequences.pointless._, AnyCoproduct._
import shapeless.{Nat, Succ}, Nat._
import shapeless.test.illTyped

object CoproductTestContext {

  object SI extends (String :+: Int :+: CNil)
  val si = new (String :+: Int :+: CNil)
}

class CoproductTests extends org.scalatest.FunSuite {

  import CoproductTestContext._

  test("create coproduct values") {

    val uh = SI at(_1, 343)
    val uhoh = SI at(_2, "lalala")

    val uhoh2 = si at(_2, "oh no yeah")

    illTyped("""

      val nnn = SI at(_2, true)
    """)

    illTyped("""

      val nnn = si at(_2, true)
    """)
  }
}