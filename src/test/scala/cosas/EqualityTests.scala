package ohnosequences.cosas.tests

import ohnosequences.cosas._

final class EqualsTests extends org.scalatest.FunSuite {

  test("basic type equality") {

    type string = String
    type stringAgain = String
    type lstring = List[String]
    type llstring = List[List[String]]
    type llstringAgain = List[lstring]
    // import coercion._

    val z = implicitly[string <≃> stringAgain]
    // get an instance
    val eqString = z(Left(""))

    implicitly[stringAgain <≃> string]
    implicitly[string <≃> string]
    implicitly[stringAgain <≃> stringAgain]

    implicitly[lstring <≃> List[String]]
    implicitly[List[String] <≃> lstring]
    implicitly[llstring <≃> llstringAgain]
    implicitly[llstringAgain <≃> llstring]
    implicitly[List[llstringAgain] <≃> List[List[List[String]]]]
    implicitly[Map[String,Boolean] <≃> Map[String,Boolean] ]
    implicitly[Map[_,_] <≃> Map[_,_]]
    case object two
    val y = two
    implicitly[two.type <≃> y.type]
  }

  test("using type equality at the instance level") {

    def doSomething[X,Y](x: X, y: Y)(implicit id: X ≃ Y): Y = id.inL(x)
    def doSomethingDifferently[X,Y](x: X, y: Y)(implicit id: X <≃> Y): X = x
    def doSomethingElse[X,Y](x: X, y: Y)(implicit ev: Y <≃> X): Y = y

    val x: String = doSomething("buh", "boh")
    def uh: String =
      doSomethingElse(
        doSomething(
          doSomethingDifferently("buh","boh"),
          doSomething("boh","buh")
        ),
        "oh"
      )

    final case class XList[A](xs: List[A]) {

      def sum(implicit ev: List[Int] ≃ List[A]): Int = {

        import ev._
        (xs: List[Int]).foldLeft(0)(_ + _)
      }

      def sum2(implicit ev: List[A] ≃ List[Int]): Int = {

        import ev._
        (xs: List[Int]).foldLeft(0)(_ + _)
      }
    }

    val xl = XList(List(1,2))
    val z = xl.sum
    val z2 = xl.sum2

    assert(z === z2)
  }

  test("distinct types, subtypes") {

    trait Uh; class Oh extends Uh

    val neq = implicitly[Uh != Oh]

    assertTypeError { """ implicitly[ Uh != Uh ] """ }

    val x: Oh = new Oh
    val p: Oh ≤ Uh = implicitly[Oh ≤ Uh]

    assert { (x: Uh) === p.asRight(x) }
  }


}
