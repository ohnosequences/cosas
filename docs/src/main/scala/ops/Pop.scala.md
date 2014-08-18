
## Popping an element from a set

It's like `Lookup`, but it removes the element



```scala
package ohnosequences.typesets


trait Pop[S <: TypeSet, E] {
  type SOut <: TypeSet
  type EOut <: E
  type Out = (EOut, SOut)
  def apply(s: S): Out
}

object Pop extends Pop_2 {
  type Aux[S <: TypeSet, E, SO <: TypeSet, EO <: E] = Pop[S, E] {
    type SOut = SO
    type EOut = EO
  } 

  type SAux[S <: TypeSet, E, SO <: TypeSet] = Pop[S, E] {
    type SOut = SO
  } 

  type EAux[S <: TypeSet, E, EO <: E] = Pop[S, E] {
    type EOut = EO
  } 


  implicit def foundInHead[E, H <: E , T <: TypeSet]: Pop.Aux[H :~: T, E, T, H] =
    new Pop[H :~: T, E] { 
      type SOut = T
      type EOut = H
      def apply(s: H :~: T): Out = (s.head, s.tail)
    }
}

trait Pop_2 {
  implicit def foundInTail[H, T <: TypeSet, E](implicit e: E ? T, l: Pop[T, E]):
    Pop.Aux[H :~: T, E, H :~: l.SOut, l.EOut] =
    new Pop[H :~: T, E] { 
      type SOut = H :~: l.SOut
      type EOut = l.EOut
      def apply(s: H :~: T): Out = {

        // val (e, t) = l(s.tail)
        // (e, s.head :~: t)

        val tpl = l(s.tail)
        (tpl._1, s.head :~: tpl._2)
      }
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