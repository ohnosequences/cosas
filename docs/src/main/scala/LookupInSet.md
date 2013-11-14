### Index

+ src
  + main
    + scala
      + [LookupInSet.scala](LookupInSet.md)
      + [MapFoldSets.scala](MapFoldSets.md)
      + [package.scala](package.md)
      + [SubtractSets.scala](SubtractSets.md)
      + [TypeSet.scala](TypeSet.md)
      + [TypeUnion.scala](TypeUnion.md)
      + [UnionSets.scala](UnionSets.md)
  + test
    + scala
      + [TypeSetTests.scala](../../test/scala/TypeSetTests.md)

------

## Lookup in TypeSet by type

This is similar to the shapeless `Selector`, but it can find first occurence of an element, 
that has a type, which is a sub-type of the given one:

```scala
trait Foo
object Bar extends Foo

(1 :~: "abc" :~: 'a' :~: Bar :~: 42).lookup[Foo] == Bar
```

------


```scala
package ohnosequences.typesets


trait Lookup[S <: TypeSet, E] { type Out
  def apply(s: S): Out
}

object Lookup extends Lookup_2 {
  implicit def foundInHead[E, H <: E , T <: TypeSet] = 
    new Lookup[H :~: T, E] { type Out = H
      def apply(s: H :~: T) = s.head
    }
}

trait Lookup_2 {
  implicit def foundInTail[H, T <: TypeSet, E](implicit e: E ∈ T, l: Lookup[T, E]) =
    new Lookup[H :~: T, E] { type Out = l.Out
      def apply(s: H :~: T) = l(s.tail)
    }
}

```

