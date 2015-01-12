package ohnosequences.scala.tests

import ohnosequences.cosas._, equals._

final class EqualsTests extends org.scalatest.FunSuite {

  test("basic type equality") {

    type string = String
    type stringAgain = String
    type lstring = List[String]
    type llstring = List[List[String]]
    type llstringAgain = List[lstring]
    // import coercion._  
    
    implicitly[string <≃> stringAgain]
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
    object two
    val y = two
    implicitly[two.type <≃> y.type]

    def doSomething[X,Y](x: X, y: Y)(implicit id: X ≃ Y): Y = id.inL(x)
    def doSomethingDifferently[X,Y](x: X, y: Y)(implicit id: X <≃> Y): X = x
    def doSomethingElse[X,Y](x: X, y: Y)(implicit ev: Y <≃> X): Y = y

    val x: String = doSomething("buh", "boh")
    def uh = doSomethingElse(doSomething(doSomethingDifferently("buh","boh"), doSomething("boh","buh")), "oh")

    final case class XList[A](xs: List[A]) {

      def sum(implicit ev: List[Int] ≃ List[A]): Int = {

        import ev._
        (xs:List[Int]).foldLeft(0)(_ + _)
      }

      def sum2(implicit ev: List[A] ≃ List[Int]): Int = {

        import ev._
        (xs:List[Int]).foldLeft(0)(_ + _)
      }      
    }

    val xl = XList(List(1,2))
    val z = xl.sum
    val z2 = xl.sum2

    assert(z === z2)

    object gadt {

      object AnyFoo {

        type Foo[A0,B0] = AnyFoo { type A = A0; type B = B0 }
      }
      sealed trait AnyFoo {

        type A
        type B
      }

      final case class X[A0]() extends AnyFoo {

        type A = A0
        type B = A
      }

      final case class Y[A0, B0](a: A0, b: B0) extends AnyFoo {

        type A = A0
        type B = B0
      }

      import AnyFoo.Foo

      def hoge1[F[_, _], A, B, C](foo: Foo[A,B], bar: F[A, C]): F[B, C] = foo match {

        case X() => bar
      }

      val x = X[Int]

      val z = hoge1(x, Y(123, "hola")) 
      assert(z === "hola")
    }
  }
}