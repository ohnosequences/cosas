package ohnosequences.cosas.tests

import ohnosequences.cosas._, elgot._

class ElgotTests extends org.scalatest.FunSuite {

  val factorial = Elgot[Int, (Int,Int), Int](
    init = x => recurse((x,1)),
    iter = { case (rest, acc) => if( rest == 0 ) output(acc) else recurse( (rest - 1, rest * acc) ) }
  )

  val multiply = Elgot[(Int,Int), (Int, Int, Int), Int](
    init = { case (x, y) => recurse( (x, y, 0) ) },
    iter = { case (x, y, p) =>
      if( x == 0 )
        output(p)
      else
        recurse( (x - 1, y, p + y) ) }
  )

  val sumL = Elgot[List[Int], (List[Int], Int), Int](
    init = { xs => recurse(xs, 0) },
    iter = { case (rest, acc) => rest match {
        case Nil    => output(acc)
        case h :: t => recurse(t, h + acc)
      }
    }
  )

  def reverseL[X] = Elgot[List[X], (List[X], List[X]), List[X]](
    init = { xs => recurse(xs, Nil) },
    iter = { case (rest, acc) => rest match {
        case Nil    => output(acc)
        case h :: t => recurse(t, h :: acc)
      }
    }
  )

  test("elgot factorial") {

    assert { factorial.tailrec(4) === 24 }
    assert { factorial.tailrec(6) === 720 }
  }

  test("elgot multiplication") {

    assert { multiply.tailrec(4,5) === 4 * 5 }
    assert { multiply.tailrec(123124, 545234) === 123124 * 545234 }
  }

  test("elgot sumL") {

    assert { sumL.tailrec(List.range(1, 10000)) === List.range(1, 10000).sum }
  }

  test("elgot reverseL") {

    assert { reverseL.tailrec(List.range(1, 10000)) === List.range(1, 10000).reverse }
  }
}
