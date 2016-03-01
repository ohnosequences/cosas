package ohnosequences.cosas.tests

import ohnosequences.cosas._, elgot._

class ElgotTests extends org.scalatest.FunSuite {

  import scala.collection.mutable.ListBuffer

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
    iter = {
      case (Nil,    acc) => output(acc)
      case (h :: t, acc) => recurse(t, h + acc)
    }
  )

  // List functions

  def reverseStd[X](l: List[X]): List[X] = {

    @scala.annotation.tailrec
    def aux(xs: List[X], acc: ListBuffer[X]): List[X] = xs match {

      case Nil => acc.toList
      case h :: t => aux(t, h +=: acc)
    }

    aux(l, new ListBuffer())
  }

  def reverseL[X] = Elgot[List[X], (List[X], ListBuffer[X]), List[X]](
    init = { xs => recurse(xs, new ListBuffer()) },
    iter = { case (rest, acc) => rest match {
        case Nil    => output(acc.toList)
        case h :: t => recurse(t, h +=: acc)
      }
    }
  )

  def filter[X](p: X => Boolean) = Elgot[List[X], (List[X], ListBuffer[X]), List[X]](

    init = { xs => recurse(xs, new ListBuffer()) },
    iter = {
      case (Nil,    acc) => output(acc.reverse.toList)
      case (h :: t, acc) => recurse(t, if( p(h) ) h +=: acc else acc )
    }
  )

  def filterAndRev[X](p: X => Boolean) = Elgot[List[X], (List[X], ListBuffer[X]), List[X]](

    init = { xs => recurse(xs, new ListBuffer()) },
    iter = {
      case (Nil,    acc) => output(acc.toList)
      case (h :: t, acc) => recurse(t, if( p(h) ) h +=: acc else acc )
    }
  )

  def map[X,Y](f: X => Y) = Elgot[List[X], (List[X], ListBuffer[Y]), List[Y]](

    init = { xs => recurse( xs, new ListBuffer() ) },
    iter = {
      case (Nil,    acc) => output( acc.toList )
      case (h :: t, acc) => recurse(t, acc += f(h))
    }
  )

  def altFilter[X](p: X => Boolean) = filterAndRev(p) andThen reverseL

  def filterIter[X](p: X => Boolean) = Elgot[List[X], (List[X], Iterator[X]), List[X]](
    init = { xs => recurse((xs, Iterator.empty)) },
    iter = {
      case (Nil,    acc) => output(acc.toList)
      case (h :: t, acc) => recurse(t, if( p(h) ) acc ++ Iterator.single(h) else acc )
    }

  )












  val isEven = { x: Int => x % 2 == 0 }
  val bigList = List.range(1, 100000)
  val reallyBigList = List.range(1, 1000000)
  val twice = { x: Int => 2 * x }



  test("elgot factorial") {

    assert { factorial.tailrec(4) === 24 }
    assert { factorial.tailrec(6) === 720 }
  }

  test("elgot multiplication") {

    assert { multiply.tailrec(4,5) === 4 * 5 }
    assert { multiply.tailrec(123124, 545234) === 123124 * 545234 }
  }

  test("elgot sumL") {

    assert { sumL.tailrec(List.range(1, 100000)) === List.range(1, 100000).sum }
  }

  test("method reverse") {

    for {i <- 1 to 100} {

      reverseStd(reallyBigList)
    }
  }

  test("elgot reverse") {

    for {i <- 1 to 100} {

      reverseL.tailrec(reallyBigList)
    }
  }

  test("std reverse") {

    for {i <- 1 to 100} {

      reallyBigList.reverse
    }
  }


  test("elgot filter") {

    assert { filter(isEven).tailrec(bigList) === filter(isEven).tailrec(bigList) }
  }

  test("std filter"){

    assert { bigList.filter(isEven) === bigList.filter(isEven) }

  }

  test("iterator filter") {

    assert { bigList.filter(isEven) === filterIter(isEven).tailrec(bigList) }
  }
  //
  // test("elgot alt filter") {
  //
  //   for {i <- 1 to 1500} {
  //
  //     (map(twice) andThen filter(isEven)).fun(bigList)
  //   }
  // }
  //
  // test("std alt filter") {
  //
  //   for {i <- 1 to 1500} {
  //
  //     bigList.map(twice).filter(isEven)
  //   }
  // }

  test("elgot fusion") {

    val buh = (map({i: Int => i * 3 + 7}) andThen filter(isEven) andThen map({x: Int => 3*x + 4*x + 7}) andThen filter(isEven))

    val z = buh.tailrec

    for {i <- 1 to 500} {

      buh.fun(bigList)
    }
  }

  test("std fusion") {

    for {i <- 1 to 500} {

      bigList
        .map({i: Int => i * 3 + 7})
        .filter(isEven)
        .map({x: Int => 3*x + 4*x + 7})
        .filter(isEven)
    }
  }

  // test("std filter reallyBigList") {
  //
  //   assert { reallyBigList.filter(isEven) === reallyBigList.filter(isEven) }
  // }
}
