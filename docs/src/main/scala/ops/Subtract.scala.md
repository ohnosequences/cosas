## Subtract one set from another

```scala
package ohnosequences.typesets


@annotation.implicitNotFound(msg = "Can't subtract ${Q} from ${S}")
trait Subtract[S <: TypeSet, Q <: TypeSet] {
  type Out <: TypeSet
  def apply(s: S, q: Q): Out
}
```

* Case when S is inside Q => result is ?:

```scala
object Subtract extends SubtractSets_2 {
  type Aux[S <: TypeSet, Q <: TypeSet, O <: TypeSet] = Subtract[S, Q] { type Out = O }

  implicit def sInQ[S <: TypeSet, Q <: TypeSet]
    (implicit e: S ? Q) = 
      new Subtract[S,    Q] { type Out = ?
          def apply(s: S, q: Q) = ?
      }
}
```

* Case when Q is empty => result is S:

```scala
trait SubtractSets_2 extends SubtractSets_3 {
  implicit def qEmpty[S <: TypeSet] = 
    new Subtract[S,    ?] { type Out = S
        def apply(s: S, q: ?) = s
    }
```

* Case when S.head ? Q => result is S.tail \ Q:

```scala
  implicit def sConsWithoutHead[H, T <: TypeSet,  Q <: TypeSet] 
    (implicit h: H ? Q, rest: T \ Q) = 
      new Subtract[H :~: T,    Q] { type Out = rest.Out
          def apply(s: H :~: T, q: Q) = rest(s.tail, q)
      }
}
```

* Case when we just leave S.head and traverse further:

```scala
trait SubtractSets_3 {
  implicit def sConsAnyHead[H, T <: TypeSet, Q <: TypeSet] 
    (implicit rest: T \ Q) =
      new Subtract[H :~: T,    Q] { type Out = H :~: rest.Out
          def apply(s: H :~: T, q: Q) = s.head :~: rest(s.tail, q)
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