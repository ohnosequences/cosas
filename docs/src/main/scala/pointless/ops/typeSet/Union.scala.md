## Sum/union of two type sets

```scala
package ohnosequences.pointless.ops.typeSet

import ohnosequences.pointless._, AnyFn._, typeSet._

@annotation.implicitNotFound(msg = "Can't union ${S} with ${Q}")
trait Union[S <: AnyTypeSet, Q <: AnyTypeSet] extends Fn2[S, Q] with WithCodomain[AnyTypeSet]
```

* Case when S is a subset of Q => just Q:

```scala
object Union extends UnionSets_2 {

  def apply[S <: AnyTypeSet, Q <: AnyTypeSet]
    (implicit uni: Union[S, Q]): Union[S, Q] with out[uni.Out] = uni

  implicit def sInQ[S <: AnyTypeSet, Q <: AnyTypeSet]
    (implicit e: S ⊂ Q): Union[S, Q] with out[Q] =
      new Union[S, Q] {
        type Out = Q
        def apply(s: S, q: Q) = q
      }
}
```

* (Dual) case when Q is a subset of S => just S:

```scala
trait UnionSets_2 extends UnionSets_3 {
  implicit def qInS[S <: AnyTypeSet, Q <: AnyTypeSet]
    (implicit e: Q ⊂ S): Union[S, Q] with out[S] =
      new Union[S, Q] { type Out = S
        def apply(s: S, q: Q) = s
      }
}
```

* Case when S.head is in Q => throwing it away:

```scala
trait UnionSets_3 extends UnionSets_4 {
  implicit def sConsWithoutHead[SH, ST <: AnyTypeSet,  Q <: AnyTypeSet]
    (implicit sh: SH ∈ Q, rest: ST ∪ Q): Union[SH :~: ST, Q] with out[rest.Out] =
      new Union[SH :~: ST, Q] {
        type Out = rest.Out
        def apply(s: SH :~: ST, q: Q) = rest(s.tail, q)
      }
}
```

* (Dual) case when Q.head is in S => throwing it away:

```scala
trait UnionSets_4 extends UnionSets_5 {
  implicit def qConsWithoutHead[S <: AnyTypeSet,  QH, QT <: AnyTypeSet]
    (implicit qh: QH ∈ S, rest: S ∪ QT): Union[S, QH :~: QT] with out[rest.Out] =
      new Union[S, QH :~: QT] {
        type Out = rest.Out
        def apply(s: S, q: QH :~: QT) = rest(s, q.tail)
      }
}
```

* Otherwise both heads are new => adding both:

```scala
trait UnionSets_5 {
  implicit def newHeads[SH, ST <: AnyTypeSet,  QH, QT <: AnyTypeSet]
    (implicit rest: ST ∪ QT): Union[SH :~: ST, QH :~: QT] with out[SH :~: QH :~: rest.Out] =
      new Union[SH :~: ST, QH :~: QT] {
        type Out = SH :~: QH :~: rest.Out
        def apply(s: SH :~: ST, q: QH :~: QT) = s.head :~: q.head :~: rest(s.tail, q.tail)
      }
}

```


------

### Index

+ src
  + test
    + scala
      + pointless
        + [PropertyTests.scala][test/scala/pointless/PropertyTests.scala]
        + [RecordTests.scala][test/scala/pointless/RecordTests.scala]
        + [TypeSetTests.scala][test/scala/pointless/TypeSetTests.scala]
  + main
    + scala
      + pointless
        + [TaggedType.scala][main/scala/pointless/TaggedType.scala]
        + [Record.scala][main/scala/pointless/Record.scala]
        + ops
          + typeSet
            + [Lookup.scala][main/scala/pointless/ops/typeSet/Lookup.scala]
            + [Reorder.scala][main/scala/pointless/ops/typeSet/Reorder.scala]
            + [Conversions.scala][main/scala/pointless/ops/typeSet/Conversions.scala]
            + [Subtract.scala][main/scala/pointless/ops/typeSet/Subtract.scala]
            + [Pop.scala][main/scala/pointless/ops/typeSet/Pop.scala]
            + [Representations.scala][main/scala/pointless/ops/typeSet/Representations.scala]
            + [Replace.scala][main/scala/pointless/ops/typeSet/Replace.scala]
            + [Take.scala][main/scala/pointless/ops/typeSet/Take.scala]
            + [Union.scala][main/scala/pointless/ops/typeSet/Union.scala]
            + [Mappers.scala][main/scala/pointless/ops/typeSet/Mappers.scala]
          + record
            + [Update.scala][main/scala/pointless/ops/record/Update.scala]
            + [Conversions.scala][main/scala/pointless/ops/record/Conversions.scala]
            + [Get.scala][main/scala/pointless/ops/record/Get.scala]
        + [Denotation.scala][main/scala/pointless/Denotation.scala]
        + [TypeUnion.scala][main/scala/pointless/TypeUnion.scala]
        + [Fn.scala][main/scala/pointless/Fn.scala]
        + [Property.scala][main/scala/pointless/Property.scala]
        + [TypeSet.scala][main/scala/pointless/TypeSet.scala]

[test/scala/pointless/PropertyTests.scala]: ../../../../../test/scala/pointless/PropertyTests.scala.md
[test/scala/pointless/RecordTests.scala]: ../../../../../test/scala/pointless/RecordTests.scala.md
[test/scala/pointless/TypeSetTests.scala]: ../../../../../test/scala/pointless/TypeSetTests.scala.md
[main/scala/pointless/TaggedType.scala]: ../../TaggedType.scala.md
[main/scala/pointless/Record.scala]: ../../Record.scala.md
[main/scala/pointless/ops/typeSet/Lookup.scala]: Lookup.scala.md
[main/scala/pointless/ops/typeSet/Reorder.scala]: Reorder.scala.md
[main/scala/pointless/ops/typeSet/Conversions.scala]: Conversions.scala.md
[main/scala/pointless/ops/typeSet/Subtract.scala]: Subtract.scala.md
[main/scala/pointless/ops/typeSet/Pop.scala]: Pop.scala.md
[main/scala/pointless/ops/typeSet/Representations.scala]: Representations.scala.md
[main/scala/pointless/ops/typeSet/Replace.scala]: Replace.scala.md
[main/scala/pointless/ops/typeSet/Take.scala]: Take.scala.md
[main/scala/pointless/ops/typeSet/Union.scala]: Union.scala.md
[main/scala/pointless/ops/typeSet/Mappers.scala]: Mappers.scala.md
[main/scala/pointless/ops/record/Update.scala]: ../record/Update.scala.md
[main/scala/pointless/ops/record/Conversions.scala]: ../record/Conversions.scala.md
[main/scala/pointless/ops/record/Get.scala]: ../record/Get.scala.md
[main/scala/pointless/Denotation.scala]: ../../Denotation.scala.md
[main/scala/pointless/TypeUnion.scala]: ../../TypeUnion.scala.md
[main/scala/pointless/Fn.scala]: ../../Fn.scala.md
[main/scala/pointless/Property.scala]: ../../Property.scala.md
[main/scala/pointless/TypeSet.scala]: ../../TypeSet.scala.md