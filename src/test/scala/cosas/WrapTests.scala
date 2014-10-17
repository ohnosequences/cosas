package ohnosequences.cosas.tests

import shapeless.test.illTyped
import ohnosequences.cosas._, AnyWrap._, ValueOf._, AnySubsetType._

object WrapTestsContext {

  case object Color extends Wrap[String]

  case class NEList[E]() extends SubsetType[List[E]] {

    val predicate: List[E] => Boolean = l => ! l.isEmpty
  }

  def NEListOf[E]: NEList[E] = NEList()
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

    val buh: Option[ValueOf[NEList[String]]] = NEListOf[String]("there's something!" :: Nil)

    val oh = NEListOf[Int](12 :: 232 :: Nil)
  }
}
