
## Replace elements in one set with elements from another

The idea is that if `Q ⊂ S`, then you can replace some elements of `S`, 
by the elements of `Q` with corresponding types. For example 
`(1 :~: 'a' :~: "foo" :~: ∅) replace ("bar" :~: 2 :~: ∅) == (2 :~: 'a' :~: "bar" :~: ∅)`. 
Note that the type of the result is the same (`S`).



```scala
package ohnosequences.cosas.ops.typeSets

import ohnosequences.cosas._, fns._, typeSets._

@annotation.implicitNotFound(msg = "Can't replace elements in ${S} with ${Q}")
trait Replace[S <: AnyTypeSet, Q <: AnyTypeSet] extends Fn2[S, Q] with Out[S]

object Replace extends Replace_2 {

  def apply[S <: AnyTypeSet, Q <: AnyTypeSet]
    (implicit replace: Replace[S, Q]): Replace[S, Q] = replace

  implicit def empty[S <: AnyTypeSet]:
        Replace[S, ∅] = 
    new Replace[S, ∅] { def apply(s: S, q: ∅) = s }

  implicit def replaceHead[H, T <: AnyTypeSet, Q <: AnyTypeSet, QOut <: AnyTypeSet]
    (implicit 
      pop: PopSOut[Q, H, QOut],
      rest: Replace[T, QOut]
    ):  Replace[H :~: T, Q] =
    new Replace[H :~: T, Q] {

      def apply(s: H :~: T, q: Q): H :~: T = {
        val (h, qq) = pop(q)
        h :~: rest(s.tail, qq)
      }
    }
}

trait Replace_2 {
  implicit def skipHead[H, T <: AnyTypeSet, Q <: AnyTypeSet, QOut <: AnyTypeSet]
    (implicit rest: Replace[T, Q]):
        Replace[H :~: T, Q] =
    new Replace[H :~: T, Q] {

      def apply(s: H :~: T, q: Q) = s.head :~: rest(s.tail, q)
    }
}

```




[test/scala/cosas/asserts.scala]: ../../../../../test/scala/cosas/asserts.scala.md
[test/scala/cosas/DenotationTests.scala]: ../../../../../test/scala/cosas/DenotationTests.scala.md
[test/scala/cosas/SubsetTypesTests.scala]: ../../../../../test/scala/cosas/SubsetTypesTests.scala.md
[test/scala/cosas/EqualityTests.scala]: ../../../../../test/scala/cosas/EqualityTests.scala.md
[test/scala/cosas/PropertyTests.scala]: ../../../../../test/scala/cosas/PropertyTests.scala.md
[test/scala/cosas/RecordTests.scala]: ../../../../../test/scala/cosas/RecordTests.scala.md
[test/scala/cosas/TypeSetTests.scala]: ../../../../../test/scala/cosas/TypeSetTests.scala.md
[test/scala/cosas/TypeUnionTests.scala]: ../../../../../test/scala/cosas/TypeUnionTests.scala.md
[main/scala/cosas/typeUnions.scala]: ../../typeUnions.scala.md
[main/scala/cosas/properties.scala]: ../../properties.scala.md
[main/scala/cosas/records.scala]: ../../records.scala.md
[main/scala/cosas/fns.scala]: ../../fns.scala.md
[main/scala/cosas/types.scala]: ../../types.scala.md
[main/scala/cosas/typeSets.scala]: ../../typeSets.scala.md
[main/scala/cosas/ops/records/Conversions.scala]: ../records/Conversions.scala.md
[main/scala/cosas/ops/records/Update.scala]: ../records/Update.scala.md
[main/scala/cosas/ops/records/Transform.scala]: ../records/Transform.scala.md
[main/scala/cosas/ops/records/Get.scala]: ../records/Get.scala.md
[main/scala/cosas/ops/typeSets/Conversions.scala]: Conversions.scala.md
[main/scala/cosas/ops/typeSets/Filter.scala]: Filter.scala.md
[main/scala/cosas/ops/typeSets/Subtract.scala]: Subtract.scala.md
[main/scala/cosas/ops/typeSets/Mappers.scala]: Mappers.scala.md
[main/scala/cosas/ops/typeSets/Union.scala]: Union.scala.md
[main/scala/cosas/ops/typeSets/Reorder.scala]: Reorder.scala.md
[main/scala/cosas/ops/typeSets/Take.scala]: Take.scala.md
[main/scala/cosas/ops/typeSets/Representations.scala]: Representations.scala.md
[main/scala/cosas/ops/typeSets/Pop.scala]: Pop.scala.md
[main/scala/cosas/ops/typeSets/Replace.scala]: Replace.scala.md
[main/scala/cosas/equality.scala]: ../../equality.scala.md