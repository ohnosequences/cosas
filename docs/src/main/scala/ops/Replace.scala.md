
## Replace elements in one set with elements from another

The idea is that if `Q ? S`, then you can replace some elements of `S`, 
by the elements of `Q` with corresponding types. For example 
`(1 :~: 'a' :~: "foo" :~: ?) replace ("bar" :~: 2 :~: ?) == (2 :~: 'a' :~: "bar" :~: ?)`. 
Note that the type of the result is the same (`S`).



```scala
package ohnosequences.typesets


@annotation.implicitNotFound(msg = "Can't replace elements in ${S} with ${Q}")
trait Replace[S <: TypeSet, Q <: TypeSet] {
  def apply(s: S, q: Q): S
}

object Replace extends Replace_2 {

  implicit def empty[S <: TypeSet]:
        Replace[S, ?] = 
    new Replace[S, ?] { def apply(s: S, q: ?) = s }

  implicit def replaceHead[H, T <: TypeSet, Q <: TypeSet, QOut <: TypeSet]
    (implicit 
      pop: Pop.Aux[Q, H, QOut, H],
      rest: Replace[T, QOut]
    ):  Replace[H :~: T, Q] =
    new Replace[H :~: T, Q] {
      def apply(s: H :~: T, q: Q): H :~: T = {
        
        // val (h, qout) = pop(q)
        // h :~: rest(s.tail, qout)
        val tpl = pop(q)
        tpl._1 :~: rest(s.tail, tpl._2)
      }
    }
}

trait Replace_2 {
  implicit def skipHead[H, T <: TypeSet, Q <: TypeSet, QOut <: TypeSet]
    (implicit rest: Replace[T, Q]):
        Replace[H :~: T, Q] =
    new Replace[H :~: T, Q] {
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