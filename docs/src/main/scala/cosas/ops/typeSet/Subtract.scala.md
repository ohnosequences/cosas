## Subtract one set from another

```scala
package ohnosequences.cosas.ops.typeSet

import ohnosequences.cosas._, AnyFn._, AnyTypeSet._

@annotation.implicitNotFound(msg = "Can't subtract ${Q} from ${S}")
trait Subtract[S <: AnyTypeSet, Q <: AnyTypeSet] extends Fn2[S, Q] with OutBound[AnyTypeSet]
```

* Case when S is inside Q => result is ∅:

```scala
object Subtract extends SubtractSets_2 {

  def apply[S <: AnyTypeSet, Q <: AnyTypeSet]
    (implicit sub: S Subtract Q): S Subtract Q = sub

  implicit def sInQ[S <: AnyTypeSet.SubsetOf[Q], Q <: AnyTypeSet]:
        (S Subtract Q) with Out[∅] = 
    new (S Subtract Q) with Out[∅] { def apply(s: S, q: Q) = ∅ }
}

trait SubtractSets_2 extends SubtractSets_3 {
```

* Case when Q is empty => result is S:

```scala
  implicit def qEmpty[S <: AnyTypeSet]: 
        (S Subtract ∅) with Out[S] =
    new (S Subtract ∅) with Out[S] { def apply(s: S, q: ∅) = s }
```

* Case when S.head ∈ Q => result is S.tail \ Q:

```scala
  implicit def sConsWithoutHead[H, T <: AnyTypeSet,  Q <: AnyTypeSet, TO <: AnyTypeSet] 
    (implicit 
      h: H ∈ Q, 
      rest: (T \ Q) { type Out = TO }
    ):  ((H :~: T) Subtract Q) with Out[TO] =
    new ((H :~: T) Subtract Q) with Out[TO] { def apply(s: H :~: T, q: Q) = rest(s.tail, q) }
}
```

* Case when we just leave S.head and traverse further:

```scala
trait SubtractSets_3 {
  implicit def sConsAnyHead[H, T <: AnyTypeSet, Q <: AnyTypeSet, TO <: AnyTypeSet] 
    (implicit 
      h: H ∉ Q, 
      rest: (T \ Q) { type Out = TO }
    ):  ((H :~: T) Subtract Q) with Out[H :~: TO] =
    new ((H :~: T) Subtract Q) with Out[H :~: TO] { def apply(s: H :~: T, q: Q) = s.head :~: rest(s.tail, q) }
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