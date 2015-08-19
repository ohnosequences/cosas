
```scala
package ohnosequences.cosas.tests

import ohnosequences.cosas._, types._, AnySubsetType._
import ohnosequences.cosas.tests.asserts._

object nelists {

  final case class WrappedList[E]() extends Wrap[List[E]]("WrappedList")

  class NEList[E] extends SubsetType[WrappedList[E]] {

    lazy val label = "NEList"
    def predicate(l: WrappedList[E] := List[E]): Boolean = ! l.value.isEmpty

    def apply(e: E): ValueOf[NEList[E]] = new ValueOf[NEList[E]](e :: Nil)
  }

  object NEList {

    implicit def toOps[E](v: ValueOf[NEList[E]]): NEListOps[E] = new NEListOps(v.value)
    implicit def toSSTops[E](v: NEList[E]): SubsetTypeOps[WrappedList[E], NEList[E]] = new SubsetTypeOps(v)
  }

  def NEListOf[E]: NEList[E] = new NEList()

  class NEListOps[E](val l: List[E]) extends AnyVal with ValueOfSubsetTypeOps[WrappedList[E], NEList[E]] {

    def ::(x: E): ValueOf[NEList[E]] = unsafeValueOf[NEList[E]](x :: l)
  }
}


class SubsetTypesTests extends org.scalatest.FunSuite {

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

```




[test/scala/cosas/asserts.scala]: asserts.scala.md
[test/scala/cosas/DenotationTests.scala]: DenotationTests.scala.md
[test/scala/cosas/SubsetTypesTests.scala]: SubsetTypesTests.scala.md
[test/scala/cosas/EqualityTests.scala]: EqualityTests.scala.md
[test/scala/cosas/PropertyTests.scala]: PropertyTests.scala.md
[test/scala/cosas/RecordTests.scala]: RecordTests.scala.md
[test/scala/cosas/TypeSetTests.scala]: TypeSetTests.scala.md
[test/scala/cosas/TypeUnionTests.scala]: TypeUnionTests.scala.md
[main/scala/cosas/typeUnions.scala]: ../../../main/scala/cosas/typeUnions.scala.md
[main/scala/cosas/properties.scala]: ../../../main/scala/cosas/properties.scala.md
[main/scala/cosas/records.scala]: ../../../main/scala/cosas/records.scala.md
[main/scala/cosas/fns.scala]: ../../../main/scala/cosas/fns.scala.md
[main/scala/cosas/types.scala]: ../../../main/scala/cosas/types.scala.md
[main/scala/cosas/typeSets.scala]: ../../../main/scala/cosas/typeSets.scala.md
[main/scala/cosas/ops/typeSets/Conversions.scala]: ../../../main/scala/cosas/ops/typeSets/Conversions.scala.md
[main/scala/cosas/ops/typeSets/Filter.scala]: ../../../main/scala/cosas/ops/typeSets/Filter.scala.md
[main/scala/cosas/ops/typeSets/Subtract.scala]: ../../../main/scala/cosas/ops/typeSets/Subtract.scala.md
[main/scala/cosas/ops/typeSets/Mappers.scala]: ../../../main/scala/cosas/ops/typeSets/Mappers.scala.md
[main/scala/cosas/ops/typeSets/Union.scala]: ../../../main/scala/cosas/ops/typeSets/Union.scala.md
[main/scala/cosas/ops/typeSets/Reorder.scala]: ../../../main/scala/cosas/ops/typeSets/Reorder.scala.md
[main/scala/cosas/ops/typeSets/Take.scala]: ../../../main/scala/cosas/ops/typeSets/Take.scala.md
[main/scala/cosas/ops/typeSets/Representations.scala]: ../../../main/scala/cosas/ops/typeSets/Representations.scala.md
[main/scala/cosas/ops/typeSets/Pop.scala]: ../../../main/scala/cosas/ops/typeSets/Pop.scala.md
[main/scala/cosas/ops/typeSets/Replace.scala]: ../../../main/scala/cosas/ops/typeSets/Replace.scala.md
[main/scala/cosas/equality.scala]: ../../../main/scala/cosas/equality.scala.md