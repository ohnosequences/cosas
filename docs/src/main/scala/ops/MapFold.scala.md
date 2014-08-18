
## Map-folder for sets 
  
Just a copy of MapFolder for `HList`s from shapeless.


```scala
package ohnosequences.typesets

import shapeless.poly.Case

trait SetMapFolder[S <: TypeSet, R, F] {
  def apply(s: S, in: R, op: (R, R) => R): R 
}
  
object SetMapFolder {
  import shapeless._
  import poly._
  
  implicit def empty[R, F] = new SetMapFolder[?, R, F] {
    def apply(s: ?, in: R, op: (R, R) => R) = in
  }
  
  implicit def cons[H, T <: TypeSet, R, F <: Poly]
    (implicit hc: Case.Aux[F, H :: HNil, R], tf: SetMapFolder[T, R, F]) =
      new SetMapFolder[H :~: T, R, F] {
          def apply(s: H :~: T, in: R, op: (R, R) => R) = op(hc(s.head), tf(s.tail, in, op))
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