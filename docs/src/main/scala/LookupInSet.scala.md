
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


trait Lookup[S <: TypeSet, E] { type Out <: E
  def apply(s: S): Out
}

object Lookup extends Lookup_2 {
  implicit def foundInHead[E, H <: E , T <: TypeSet] = 
    new Lookup[H :~: T, E] { type Out = H
      def apply(s: H :~: T) = s.head
    }
}

trait Lookup_2 {
  implicit def foundInTail[H, T <: TypeSet, E](implicit e: E ? T, l: Lookup[T, E]) =
    new Lookup[H :~: T, E] { type Out = l.Out
      def apply(s: H :~: T) = l(s.tail)
    }
}

```


------

### Index

+ src
  + main
    + scala
      + [HListOps.scala][main/scala/HListOps.scala]
      + [LookupInSet.scala][main/scala/LookupInSet.scala]
      + [MapFoldSets.scala][main/scala/MapFoldSets.scala]
      + [package.scala][main/scala/package.scala]
      + [SetMapper.scala][main/scala/SetMapper.scala]
      + [SubtractSets.scala][main/scala/SubtractSets.scala]
      + [TypeSet.scala][main/scala/TypeSet.scala]
      + [TypeUnion.scala][main/scala/TypeUnion.scala]
      + [UnionSets.scala][main/scala/UnionSets.scala]
  + test
    + scala
      + [TypeSetTests.scala][test/scala/TypeSetTests.scala]

[main/scala/HListOps.scala]: HListOps.scala.md
[main/scala/LookupInSet.scala]: LookupInSet.scala.md
[main/scala/MapFoldSets.scala]: MapFoldSets.scala.md
[main/scala/package.scala]: package.scala.md
[main/scala/SetMapper.scala]: SetMapper.scala.md
[main/scala/SubtractSets.scala]: SubtractSets.scala.md
[main/scala/TypeSet.scala]: TypeSet.scala.md
[main/scala/TypeUnion.scala]: TypeUnion.scala.md
[main/scala/UnionSets.scala]: UnionSets.scala.md
[test/scala/TypeSetTests.scala]: ../../test/scala/TypeSetTests.scala.md