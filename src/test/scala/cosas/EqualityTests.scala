package ohnosequences.scala.tests

import ohnosequences.cosas._, equality._

object eqStuff {

  type string = String
  type stringAgain = String
  import coercion._  
  
  implicitly[string ?≃ stringAgain]
  implicitly[stringAgain ?≃ string]
  implicitly[string ?≃ string]
  implicitly[stringAgain ?≃ stringAgain]
  
  def doSomething[X,Y](x: X, y: Y)(implicit ev: X ?≃ Y): (X ≃ Y) = y
  def doSomethingDifferently[X,Y](x: X, y: Y)(implicit ev: X ?≃ Y): (X ≃ Y) = x
  def doSomethingElse[X,Y](x: X, y: Y)(implicit ev: Y ?≃ X): (Y ≃ X) = x

  val x: String = doSomething("buh", "boh")

  // I cannot remove the return type (cyclic blahblah); why?
  def uh: String = doSomethingElse(doSomething(doSomethingDifferently("buh","boh"), doSomething("boh","buh")), "oh")


  final case class XList[A](xs: List[A]) {

    def sum(implicit ev: List[A] Equality List[Int]): Int = {

      import ev._

      swap(xs).foldLeft(0)(_ + _)
    }
      
  }

  val xl = XList(List(1,2))

  val z = xl.sum
}

final class EqualityTests extends org.scalatest.FunSuite {

  test("basic type equality") {

    type string = String
    type stringAgain = String
    import coercion._  
    
    implicitly[string ?≃ stringAgain]
    implicitly[stringAgain ?≃ string]
    implicitly[string ?≃ string]
    implicitly[stringAgain ?≃ stringAgain]
    
    def doSomething[X,Y](x: X, y: Y)(implicit ev: X ?≃ Y): (X ≃ Y) = y
    def doSomethingDifferently[X,Y](x: X, y: Y)(implicit ev: X ?≃ Y): (X ≃ Y) = x

    // this fails here, due to some scalatest thing?
    // val x: String = doSomething("buh", "boh")
  }
}