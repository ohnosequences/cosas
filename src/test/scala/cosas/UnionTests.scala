package ohnosequences.cosas.tests

import ohnosequences.cosas._, unions._, typeUnions._

final class UnionTests extends org.scalatest.FunSuite {


  test("can create values") {

    type BIS = Empty[Any] Or Boolean Or Int Or String
    type BAS = Empty[Any] Or Boolean Or Any Or String

    type S = String :+: Empty[Any]
    implicitly[ S#Values ≃ (empty or ValueAt[String,_1]) ]

    implicitly[(Empty[Any] Or String) ≤ (Empty[Any] Or Any)]

    val bis1 = witness[BIS] at (witness[_1] -> true)
    val bis2 = witness[BIS] at (witness[_2] -> 3)
    val bis3 = witness[BIS] at (witness[_3] -> "hola")

    val bas3: ValueOfUnion[BAS] = bis3
    val bas1: ValueOfUnion[BAS] = bis1
  }
}
