package ohnosequences.scala.tests

import ohnosequences.cosas._, monads._, typeConstructors._

final class MonadsTests extends org.scalatest.FunSuite {

  test("default identity monad") {

    import idMonad._

    val x: String = 3 flatMap { x => x.toString }
    val z: String = 3 map { x => x.toString } flatMap { x => x.toString }
  }

  // test("default identity functor") {

  //   import idFunctor._

  //   val x: String = 3 map { x => x.toString }
  // }

  // test("default maybe functor") {

  //   import maybeFunctor._

  //   val x  = Some(3) map { x => x.toString }
  // }

}

  