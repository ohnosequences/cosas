package ohnosequences.scala.tests

import ohnosequences.cosas._, functors._

final class FunctorsTests extends org.scalatest.FunSuite {

  test("default list functor") {

    import listFunctor._

    val x: List[String] = List(1,2,3) map { x => x.toString }
  }

  test("default identity functor") {

    import idFunctor._

    val x: String = 3 map { x => x.toString }
  }

  test("default maybe functor") {

    import maybeFunctor._

    val x = Some(3) map { x => x.toString }
  }

}

  