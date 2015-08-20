
```scala
package ohnosequences.cosas.tests

case object asserts {

  import ohnosequences.cosas._, types._, equalities._
  import org.scalatest.Assertions._

  // not only compares the values, but also check the types equality (essential for tagged values)
  def checkTypedEq[A, B](a: A, b: B)(implicit typesEq: A ≃ B): Boolean = a == b

  implicit def taggedOps[T <: AnyType, V <: T#Raw](v: T := V):
    TaggedOps[T, V] =
    TaggedOps[T, V](v)

  case class TaggedOps[T <: AnyType, V <: T#Raw](v: T := V) {

    def =~=[W <: T#Raw](w: T := W)(implicit typesEq: V ≃ W): Boolean = v.value == w.value
  }

  def assertTypedEq[A, B](a: A, b: B)(implicit typesEq: A ≃ B): Unit =
    assert(a == b)

  def assertTaggedEq[TA <: AnyType, A <: TA#Raw, TB <: AnyType, B <: TB#Raw](a: TA := A, b: TB := B)
    (implicit
      tagsEq: TA ≃ TB,
      valsEq: A ≃ B
    ): Unit = assert(a.value == b.value)
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
[main/scala/cosas/ops/records/Conversions.scala]: ../../../main/scala/cosas/ops/records/Conversions.scala.md
[main/scala/cosas/ops/records/Update.scala]: ../../../main/scala/cosas/ops/records/Update.scala.md
[main/scala/cosas/ops/records/Transform.scala]: ../../../main/scala/cosas/ops/records/Transform.scala.md
[main/scala/cosas/ops/records/Get.scala]: ../../../main/scala/cosas/ops/records/Get.scala.md
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