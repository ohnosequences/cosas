package ohnosequences.cosas.tests

import ohnosequences.cosas._, klists._, ksets._, typeUnions._


class KSetTests extends org.scalatest.FunSuite {

  test("can create values of KSets") {

    val is: Int :~: String :~: ∅[Any] = 2 :~: "hola" :~: ∅[Any]

    assertTypeError {
      """
      val ii: Int :~: Int :~: ∅[Int] = 2 :~: 2 :~: ∅[Int]
      """
    }

    val isb: Boolean :~: Int :~: String :~: ∅[Any] = true :~: is
  }

}
