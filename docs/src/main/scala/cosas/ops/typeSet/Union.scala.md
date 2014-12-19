## Sum/union of two type sets

```scala
package ohnosequences.cosas.ops.typeSets

import ohnosequences.cosas._, AnyFn._, AnyTypeSet._

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


------

### Index

+ src
  + test
    + scala
      + cosas
        + [PropertyTests.scala][test/scala/cosas/PropertyTests.scala]
        + [TypeUnionTests.scala][test/scala/cosas/TypeUnionTests.scala]
        + [ScalazEquality.scala][test/scala/cosas/ScalazEquality.scala]
        + [WrapTests.scala][test/scala/cosas/WrapTests.scala]
        + [RecordTests.scala][test/scala/cosas/RecordTests.scala]
        + [TypeSetTests.scala][test/scala/cosas/TypeSetTests.scala]
  + main
    + scala
      + cosas
        + [PropertiesHolder.scala][main/scala/cosas/PropertiesHolder.scala]
        + [Record.scala][main/scala/cosas/Record.scala]
        + ops
          + typeSet
            + [Check.scala][main/scala/cosas/ops/typeSet/Check.scala]
            + [Reorder.scala][main/scala/cosas/ops/typeSet/Reorder.scala]
            + [Conversions.scala][main/scala/cosas/ops/typeSet/Conversions.scala]
            + [AggregateProperties.scala][main/scala/cosas/ops/typeSet/AggregateProperties.scala]
            + [Subtract.scala][main/scala/cosas/ops/typeSet/Subtract.scala]
            + [Pop.scala][main/scala/cosas/ops/typeSet/Pop.scala]
            + [Representations.scala][main/scala/cosas/ops/typeSet/Representations.scala]
            + [Replace.scala][main/scala/cosas/ops/typeSet/Replace.scala]
            + [Take.scala][main/scala/cosas/ops/typeSet/Take.scala]
            + [Union.scala][main/scala/cosas/ops/typeSet/Union.scala]
            + [Mappers.scala][main/scala/cosas/ops/typeSet/Mappers.scala]
          + record
            + [Update.scala][main/scala/cosas/ops/record/Update.scala]
            + [Conversions.scala][main/scala/cosas/ops/record/Conversions.scala]
            + [Get.scala][main/scala/cosas/ops/record/Get.scala]
        + [TypeUnion.scala][main/scala/cosas/TypeUnion.scala]
        + [Fn.scala][main/scala/cosas/Fn.scala]
        + [Types.scala][main/scala/cosas/Types.scala]
        + csv
          + [csv.scala][main/scala/cosas/csv/csv.scala]
        + [Property.scala][main/scala/cosas/Property.scala]
        + [TypeSet.scala][main/scala/cosas/TypeSet.scala]

[test/scala/cosas/PropertyTests.scala]: ../../../../../test/scala/cosas/PropertyTests.scala.md
[test/scala/cosas/TypeUnionTests.scala]: ../../../../../test/scala/cosas/TypeUnionTests.scala.md
[test/scala/cosas/ScalazEquality.scala]: ../../../../../test/scala/cosas/ScalazEquality.scala.md
[test/scala/cosas/WrapTests.scala]: ../../../../../test/scala/cosas/WrapTests.scala.md
[test/scala/cosas/RecordTests.scala]: ../../../../../test/scala/cosas/RecordTests.scala.md
[test/scala/cosas/TypeSetTests.scala]: ../../../../../test/scala/cosas/TypeSetTests.scala.md
[main/scala/cosas/PropertiesHolder.scala]: ../../PropertiesHolder.scala.md
[main/scala/cosas/Record.scala]: ../../Record.scala.md
[main/scala/cosas/ops/typeSet/Check.scala]: Check.scala.md
[main/scala/cosas/ops/typeSet/Reorder.scala]: Reorder.scala.md
[main/scala/cosas/ops/typeSet/Conversions.scala]: Conversions.scala.md
[main/scala/cosas/ops/typeSet/AggregateProperties.scala]: AggregateProperties.scala.md
[main/scala/cosas/ops/typeSet/Subtract.scala]: Subtract.scala.md
[main/scala/cosas/ops/typeSet/Pop.scala]: Pop.scala.md
[main/scala/cosas/ops/typeSet/Representations.scala]: Representations.scala.md
[main/scala/cosas/ops/typeSet/Replace.scala]: Replace.scala.md
[main/scala/cosas/ops/typeSet/Take.scala]: Take.scala.md
[main/scala/cosas/ops/typeSet/Union.scala]: Union.scala.md
[main/scala/cosas/ops/typeSet/Mappers.scala]: Mappers.scala.md
[main/scala/cosas/ops/record/Update.scala]: ../record/Update.scala.md
[main/scala/cosas/ops/record/Conversions.scala]: ../record/Conversions.scala.md
[main/scala/cosas/ops/record/Get.scala]: ../record/Get.scala.md
[main/scala/cosas/TypeUnion.scala]: ../../TypeUnion.scala.md
[main/scala/cosas/Fn.scala]: ../../Fn.scala.md
[main/scala/cosas/Types.scala]: ../../Types.scala.md
[main/scala/cosas/csv/csv.scala]: ../../csv/csv.scala.md
[main/scala/cosas/Property.scala]: ../../Property.scala.md
[main/scala/cosas/TypeSet.scala]: ../../TypeSet.scala.md