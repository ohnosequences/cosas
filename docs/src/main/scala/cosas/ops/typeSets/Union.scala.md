## Sum/union of two type sets

```scala
package ohnosequences.cosas.ops.typeSets

import ohnosequences.cosas._, fns._, typeSets._

@annotation.implicitNotFound(msg = "Can't union ${S} with ${Q}")
trait Union[S <: AnyTypeSet, Q <: AnyTypeSet] extends Fn2[S, Q] with OutBound[AnyTypeSet]
```

* Case when S is a subset of Q => just Q:

```scala
object Union extends UnionSets_2 {

  def apply[S <: AnyTypeSet, Q <: AnyTypeSet]
    (implicit uni: Union[S, Q]): Union[S, Q] = uni

  implicit def sInQ[S <: AnyTypeSet.SubsetOf[Q], Q <: AnyTypeSet]:
        Union[S, Q] with Out[Q] =
    new Union[S, Q] with Out[Q] { def apply(s: S, q: Q) = q }
}
```

* (Dual) case when Q is a subset of S => just S:

```scala
trait UnionSets_2 extends UnionSets_3 {
  implicit def qInS[S <: AnyTypeSet, Q <: AnyTypeSet.SubsetOf[S]]:
        Union[S, Q] with Out[S] =
    new Union[S, Q] with Out[S] { def apply(s: S, q: Q) = s }
}
```

* Case when S.head is in Q => throwing it away:

```scala
trait UnionSets_3 extends UnionSets_4 {
  implicit def sHead[SH, ST <: AnyTypeSet, Q <: AnyTypeSet, O <: AnyTypeSet]
    (implicit 
      sh: SH ∈ Q, 
      rest: (ST ∪ Q) { type Out = O }
    ):  Union[SH :~: ST, Q] with Out[O] =
    new Union[SH :~: ST, Q] with Out[O] {

      def apply(s: SH :~: ST, q: Q) = rest(s.tail, q)
    }
}
```

* (Dual) case when Q.head is in S => throwing it away:

```scala
trait UnionSets_4 extends UnionSets_5 {
  implicit def qHead[S <: AnyTypeSet, QH, QT <: AnyTypeSet, O <: AnyTypeSet]
    (implicit
      qh: QH ∈ S, 
      rest: (S ∪ QT) { type Out = O }
    ):  Union[S, QH :~: QT] with Out[O] =
    new Union[S, QH :~: QT] with Out[O] {

      def apply(s: S, q: QH :~: QT) = rest(s, q.tail)
    }
}
```

* Otherwise both heads are new => adding both:

```scala
trait UnionSets_5 {
  implicit def bothHeads[SH, ST <: AnyTypeSet, QH, QT <: AnyTypeSet, O <: AnyTypeSet]
    (implicit
      sh: SH ∉ (QH :~: QT), 
      qh: QH ∉ (SH :~: ST), 
      rest: (ST ∪ QT) { type Out = O }
    ):  Union[SH :~: ST, QH :~: QT] with Out[SH :~: QH :~: O] =
    new Union[SH :~: ST, QH :~: QT] with Out[SH :~: QH :~: O] {

      def apply(s: SH :~: ST, q: QH :~: QT) = s.head :~: q.head :~: rest(s.tail, q.tail)
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