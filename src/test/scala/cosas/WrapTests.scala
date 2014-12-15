package ohnosequences.cosas.tests

import shapeless.test.illTyped
import ohnosequences.cosas._, AnyWrap._, ValueOf._, AnySubsetType._

object WrapTestsContext {

  case object Color extends Wrap[String]

  final class WrappedList[E] extends Wrap[List[E]]
  class NEList[E] extends SubsetType[WrappedList[E]] {

    def predicate(l: List[E]): Boolean = ! l.isEmpty

    def apply(e: E): ValueOf[NEList[E]] = new ValueOf[NEList[E]](e :: Nil)
  }

  object NEList {

    implicit def toOps[E](v: ValueOf[NEList[E]]): NEListOps[E] = new NEListOps(v.raw)
  }

  def NEListOf[E]: NEList[E] = new NEList()

  class NEListOps[E](val l: List[E]) extends AnyVal with ValueOfSubsetTypeOps[WrappedList[E], NEList[E]] {

    def ::(x: E): ValueOf[NEList[E]] = unsafeValueOf[NEList[E]](x :: l)
  }
}

class WrapTests extends org.scalatest.FunSuite {
  import WrapTestsContext._

  test("creating values") {

    val azul = Color("blue")
    val verde = valueOf[Color.type]("green")
    val amarillo = Color withValue "yellow"

    assert{ azul.raw == "blue" }
    assert{ verde.raw == "green" }
    assert{ amarillo.raw == "yellow" }
  }

  test("naive nonempty lists") {

    // val buh: Option[ValueOf[NEList[String]]] = NEListOf[String]("there's something!" :: Nil)

    // val oh = NEListOf[Int](12 :: 232 :: Nil)

    val nelint = NEListOf(232)

    val u1 = 23 :: nelint
  }
}
