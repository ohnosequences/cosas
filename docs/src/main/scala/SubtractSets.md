### Index

+ src
  + main
    + scala
      + [implicits.scala](implicits.md)
      + [LookupInSet.scala](LookupInSet.md)
      + [MapFoldSets.scala](MapFoldSets.md)
      + [SubtractSets.scala](SubtractSets.md)
      + [TypeSet.scala](TypeSet.md)
      + [TypeUnion.scala](TypeUnion.md)
      + [UnionSets.scala](UnionSets.md)
  + test
    + scala
      + [TypeSetTests.scala](../../test/scala/TypeSetTests.md)

------

## Subtract one set from another

```scala
package ohnosequences.typesets

import ohnosequences.typesets.implicits._

trait SubtractSets[S <: TypeSet, Q <: TypeSet] {
  type Out <: TypeSet
  def apply(s: S, q: Q): Out
}
```

* Case when S is inside Q => result is ∅:

```scala
object SubtractSets extends SubtractSets_2 {
  implicit def sInQ[S <: TypeSet, Q <: TypeSet]
    (implicit e: S ⊂ Q) = 
      new SubtractSets[S,    Q] { type Out = ∅
          def apply(s: S, q: Q) = ∅
      }
}
```

* Case when Q is empty => result is S:

```scala
trait SubtractSets_2 extends SubtractSets_3 {
  implicit def qEmpty[S <: TypeSet] = 
    new SubtractSets[S,    ∅] { type Out = S
        def apply(s: S, q: ∅) = s
    }
```

* Case when S.head ∈ Q => result is S.tail \ Q:

```scala
  implicit def sConsWithoutHead[H, T <: TypeSet,  Q <: TypeSet] 
    (implicit h: H ∈ Q, rest: T \ Q) = 
      new SubtractSets[H :~: T,    Q] { type Out = rest.Out
          def apply(s: H :~: T, q: Q) = rest(s.tail, q)
      }
}
```

* Case when we just leave S.head and traverse further:

```scala
trait SubtractSets_3 {
  implicit def sConsAnyHead[H, T <: TypeSet, Q <: TypeSet] 
    (implicit rest: T \ Q) =
      new SubtractSets[H :~: T,    Q] { type Out = H :~: rest.Out
          def apply(s: H :~: T, q: Q) = s.head :~: rest(s.tail, q)
      }
}

```

