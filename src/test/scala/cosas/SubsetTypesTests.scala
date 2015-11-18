package ohnosequences.cosas.tests

import ohnosequences.cosas._, types._, syntax.SubsetTypeSyntax
import ohnosequences.cosas.tests.asserts._
import nelists._

case object nelists {

  final case class WrappedList[E]() extends AnyType {

    type Raw = List[E]

    val label: String = "WrappedList"
  }

  class NEList[E] extends SubsetType[WrappedList[E]] {

    lazy val label: String = "NEList"
    def predicate(l: WrappedList[E] := List[E]): Boolean = ! l.value.isEmpty

    def from(e: E): NEList[E] := List[E] = new (NEList[E] := List[E])(e :: Nil)
  }

  case object NEList {

    implicit def toSyntax[E](v: NEList[E] := List[E]): NEListSyntax[E] = new NEListSyntax(v.value)
    implicit def toSSTops[E](v: NEList[E]): SubsetTypeSyntax[WrappedList[E], NEList[E]] = new SubsetTypeSyntax(v)
  }

  def NEListOf[E]: NEList[E] = new NEList[E]()

  case class NEListSyntax[E](val l: List[E]) extends AnyVal with ValueOfSubsetTypeSyntax[WrappedList[E], NEList[E]] {

    def ::(x: E): NEList[E] := List[E] = unsafeValueOf[NEList[E]](x :: l)
  }
}


class SubsetTypesTests extends org.scalatest.FunSuite {

  test("naive nonempty lists") {

    // this is Some(...) but we don't know at runtime. What about a macro for this? For literals of course
    val oh = NEListOf[Int](
      WrappedList[Int] := 12 :: 232 :: Nil
    )

    val nelint: NEList[Int] := List[Int] = NEListOf.from(232)

    val u1: NEList[Int] := List[Int] = 23 :: nelint
  }
}
