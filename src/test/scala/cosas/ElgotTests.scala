package ohnosequences.cosas.tests

import ohnosequences.cosas._, elgot._

class ElgotTests extends org.scalatest.FunSuite {

  def output[L,R](r: R) : Either[L,R] = Right(r)
  def recurse[L,R](l: L): Either[L,R] = Left(l)

  val factorial = Elgot(
    init = { x: Int => recurse((x,1)) },
    iter = { u: (Int, Int) => if( u._1 == 0 ) output(u._2) else recurse( (u._1 - 1, u._2 * u._1) ) }
  )

  val multiply = Elgot(
    init = { xy: (Int, Int) => recurse( (xy._1, xy._2, 0) ) },
    iter = { xyp: (Int, Int, Int) =>
      if( xyp._1 == 0 )
        output(xyp._3)
      else
        recurse( (xyp._1 - 1, xyp._2, xyp._3 + xyp._2) ) }
  )

  test("elgot factorial") {

    assert { factorial.tailrec(4) === 24 }
    assert { factorial.tailrec(6) === 720 }
  }

  test("elgot multiplication") {

    assert { multiply.tailrec(4,5) === 4 * 5 }
    assert { multiply.tailrec(123124, 545234) === 123124 * 545234 }
  }
}
