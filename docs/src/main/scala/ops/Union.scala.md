## Sum/union of two type sets

```scala
package ohnosequences.typesets


@annotation.implicitNotFound(msg = "Can't union ${S} with ${Q}")
trait Union[S <: TypeSet, Q <: TypeSet] {
  type Out <: TypeSet
  def apply(s: S, q: Q): Out
}
```

* Case when S is a subset of Q => just Q:

```scala
object Union extends UnionSets_2 {
  type Aux[S <: TypeSet, Q <: TypeSet, O <: TypeSet] = Union[S, Q] { type Out = O }

  implicit def sInQ[S <: TypeSet, Q <: TypeSet]
    (implicit e: S ? Q) =
      new Union[S, Q] { type Out = Q
        def apply(s: S, q: Q) = q
      }
}
```

* (Dual) case when Q is a subset of S => just S:

```scala
trait UnionSets_2 extends UnionSets_3 {
  implicit def qInS[S <: TypeSet, Q <: TypeSet]
    (implicit e: Q ? S) =
      new Union[S, Q] { type Out = S
        def apply(s: S, q: Q) = s
      }
}
```

* Case when S.head is in Q => throwing it away:

```scala
trait UnionSets_3 extends UnionSets_4 {
  implicit def sConsWithoutHead[SH, ST <: TypeSet,  Q <: TypeSet]
    (implicit sh: SH ? Q, rest: ST ? Q) =
      new Union[ SH :~: ST,    Q] { type Out = rest.Out
        def apply(s: SH :~: ST, q: Q) = rest(s.tail, q)
      }
}
```

* (Dual) case when Q.head is in S => throwing it away:

```scala
trait UnionSets_4 extends UnionSets_5 {
  implicit def qConsWithoutHead[S <: TypeSet,  QH, QT <: TypeSet]
    (implicit qh: QH ? S, rest: S ? QT) =
      new Union[ S,    QH :~: QT] { type Out = rest.Out
        def apply(s: S, q: QH :~: QT) = rest(s, q.tail)
      }
}
```

* Otherwise both heads are new => adding both:

```scala
trait UnionSets_5 {
  implicit def newHeads[SH, ST <: TypeSet,  QH, QT <: TypeSet]
    (implicit rest: ST ? QT) =
      new Union[ SH :~: ST,    QH :~: QT] { type Out = SH :~: QH :~: rest.Out
        def apply(s: SH :~: ST, q: QH :~: QT) = s.head :~: q.head :~: rest(s.tail, q.tail)
      }
}

```


------

### Index

+ src
  + main
    + scala
      + items
        + [items.scala][main/scala/items/items.scala]
      + ops
        + [Choose.scala][main/scala/ops/Choose.scala]
        + [Lookup.scala][main/scala/ops/Lookup.scala]
        + [Map.scala][main/scala/ops/Map.scala]
        + [MapFold.scala][main/scala/ops/MapFold.scala]
        + [Pop.scala][main/scala/ops/Pop.scala]
        + [Reorder.scala][main/scala/ops/Reorder.scala]
        + [Replace.scala][main/scala/ops/Replace.scala]
        + [Subtract.scala][main/scala/ops/Subtract.scala]
        + [ToList.scala][main/scala/ops/ToList.scala]
        + [Union.scala][main/scala/ops/Union.scala]
      + [package.scala][main/scala/package.scala]
      + pointless
        + impl
      + [Property.scala][main/scala/Property.scala]
      + [Record.scala][main/scala/Record.scala]
      + [Representable.scala][main/scala/Representable.scala]
      + [TypeSet.scala][main/scala/TypeSet.scala]
      + [TypeUnion.scala][main/scala/TypeUnion.scala]
  + test
    + scala
      + items
        + [itemsTests.scala][test/scala/items/itemsTests.scala]
      + [RecordTests.scala][test/scala/RecordTests.scala]
      + [TypeSetTests.scala][test/scala/TypeSetTests.scala]

[main/scala/items/items.scala]: ../items/items.scala.md
[main/scala/ops/Choose.scala]: Choose.scala.md
[main/scala/ops/Lookup.scala]: Lookup.scala.md
[main/scala/ops/Map.scala]: Map.scala.md
[main/scala/ops/MapFold.scala]: MapFold.scala.md
[main/scala/ops/Pop.scala]: Pop.scala.md
[main/scala/ops/Reorder.scala]: Reorder.scala.md
[main/scala/ops/Replace.scala]: Replace.scala.md
[main/scala/ops/Subtract.scala]: Subtract.scala.md
[main/scala/ops/ToList.scala]: ToList.scala.md
[main/scala/ops/Union.scala]: Union.scala.md
[main/scala/package.scala]: ../package.scala.md
[main/scala/Property.scala]: ../Property.scala.md
[main/scala/Record.scala]: ../Record.scala.md
[main/scala/Representable.scala]: ../Representable.scala.md
[main/scala/TypeSet.scala]: ../TypeSet.scala.md
[main/scala/TypeUnion.scala]: ../TypeUnion.scala.md
[test/scala/items/itemsTests.scala]: ../../../test/scala/items/itemsTests.scala.md
[test/scala/RecordTests.scala]: ../../../test/scala/RecordTests.scala.md
[test/scala/TypeSetTests.scala]: ../../../test/scala/TypeSetTests.scala.md