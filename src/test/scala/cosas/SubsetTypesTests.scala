package ohnosequences.cosas.tests

import ohnosequences.cosas._, types._, AnySubsetType._

object nelists {

  final case class WrappedList[E]() extends Wrap[List[E]]("WrappedList")

  class NEList[E] extends SubsetType[WrappedList[E]] {

    lazy val label = "NEList"
    def predicate(l: WrappedList[E] := List[E]): Boolean = ! l.value.isEmpty

    def apply(e: E): ValueOf[NEList[E]] = new ValueOf[NEList[E]](e :: Nil)
  }

  object NEList {

    implicit def toOps[E](v: ValueOf[NEList[E]]): NEListOps[E] = new NEListOps(v.value)
    implicit def toSSTops[E](v: NEList[E]): SubSetTypeOps[WrappedList[E], NEList[E]] = new SubSetTypeOps(v)
  }

  def NEListOf[E]: NEList[E] = new NEList()

  class NEListOps[E](val l: List[E]) extends AnyVal with ValueOfSubsetTypeOps[WrappedList[E], NEList[E]] {

    def ::(x: E): ValueOf[NEList[E]] = unsafeValueOf[NEList[E]](x :: l)
  }
}


class SubsetTypesTests extends org.scalatest.FunSuite with ScalazEquality {

  test("naive nonempty lists") {

    import nelists._
    // this is Some(...) but we don't know at runtime. What about a macro for this? For literals of course
    val oh = NEListOf[Int](
      WrappedList[Int] := 12 :: 232 :: Nil
    )

    val nelint = NEListOf(232)

    val u1: ValueOf[NEList[Int]] = 23 :: nelint
  }
}