## Subtract one set from another

```scala
package ohnosequences.cosas.ops.typeSets

import ohnosequences.cosas._, fns._, typeSets._

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