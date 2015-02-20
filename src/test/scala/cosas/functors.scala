package ohnosequences.scala.tests

import ohnosequences.cosas._, functors._

final class FunctorsTests extends org.scalatest.FunSuite {

  test("default list functor") {

    import listFunctor._

    val x: List[String] = List(1,2,3) mapp { x => x.toString }
  }

  test("default identity functor") {

    import idFunctor._

    val x: String = 3 map { x => x.toString }
  }

  test("default maybe functor") {

    import maybeFunctor._

    val x = Some(3) mapp { x => x.toString }
  }

  test("basic functor composition") {

    import maybeFunctor._, listFunctor._

    implicit object lm extends FunctorComposition(SListFunctor, MaybeFunctor)

    val x = lm.map( Some(List(3)) )( { x: Int => x.toString } )

    println(x)

    // should work after adding modules for composition (without type Annots I mean)
    // actually I'm not so sure
    val x0: Option[List[String]] = FunctorSyntax[lm.TC,Int]( Some(List(3)) ).mapp( { x: Int => x.toString } )
  }
}

  