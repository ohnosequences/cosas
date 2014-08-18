
```scala
package ohnosequences.typesets

import shapeless.record.KeyTag
```


Tagging is used for being able to operate on `Raw` values knowing what they are representing; `Rep` is just `Raw` tagged with the `.type` of this representation. So, summarizing

- `Raw` is the type used to the represent `this.type`
- `Rep <: Raw` is just `Raw` tagged with `this.type`; the `Rep`resentation


```scala
trait Representable { self =>

  type Raw

  type Me = this.type
```


`Raw` tagged with `self.type`; this lets you recognize a denotation while being able to operate on it as `Raw`.


```scala
  final type Rep = AnyTag.TaggedWith[self.type]
```


`Raw` enters, `Rep` leaves


```scala
  final def ->>(r: Raw): self.Rep = AnyTag.TagWith[self.type](self)(r)
```


This lets you get the instance of the singleton type from a tagged `Rep` value.


```scala
  implicit def fromRep(x: self.Rep): self.type = self

  implicit def otherFromRep[X <: Me](rep: X#Rep) = {

    self
  }
}
```


This trait represents a mapping between 

- members `Tpe` of a universe of types `TYPE`
- and `Raw` a type meant to be a denotation of `Tpe` thus the name



```scala
trait AnyDenotation extends Representable {
```

The base type for the types that this thing denotes

```scala
  type TYPE
  type Tpe <: TYPE
  val  tpe: Tpe
}
```


Bound the universe of types to be `T`s


```scala
trait Denotation[T] extends AnyDenotation { type TYPE = T }
```


The companion object contains mainly tagging functionality.


```scala
object AnyTag {

  case class TagWith[D <: Singleton with Representable](val d: D) {

    def apply(dr : d.Raw): TaggedWith[D] = {

      dr.asInstanceOf[TaggedWith[D]]
    }
  }

  type TaggedWith[D <: Representable] = D#Raw with Tag[D]

  // Has to be empty! See http://www.scala-lang.org/old/node/11165.html#comment-49097
  sealed trait AnyTag {}
  sealed trait Tag[D <: Representable] extends AnyTag with KeyTag[D, D#Raw] {}

  type RawOf[D <: Singleton with Representable] = D#Raw

  type AsRepOf[X, D <: Singleton with Representable] = X with D#Raw with Tag[D]

  type SingletonOf[X <: AnyRef] = Singleton with X
  type Type[X <: AnyRef] = Singleton with X

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

[main/scala/items/items.scala]: items/items.scala.md
[main/scala/ops/Choose.scala]: ops/Choose.scala.md
[main/scala/ops/Lookup.scala]: ops/Lookup.scala.md
[main/scala/ops/Map.scala]: ops/Map.scala.md
[main/scala/ops/MapFold.scala]: ops/MapFold.scala.md
[main/scala/ops/Pop.scala]: ops/Pop.scala.md
[main/scala/ops/Reorder.scala]: ops/Reorder.scala.md
[main/scala/ops/Replace.scala]: ops/Replace.scala.md
[main/scala/ops/Subtract.scala]: ops/Subtract.scala.md
[main/scala/ops/ToList.scala]: ops/ToList.scala.md
[main/scala/ops/Union.scala]: ops/Union.scala.md
[main/scala/package.scala]: package.scala.md
[main/scala/Property.scala]: Property.scala.md
[main/scala/Record.scala]: Record.scala.md
[main/scala/Representable.scala]: Representable.scala.md
[main/scala/TypeSet.scala]: TypeSet.scala.md
[main/scala/TypeUnion.scala]: TypeUnion.scala.md
[test/scala/items/itemsTests.scala]: ../../test/scala/items/itemsTests.scala.md
[test/scala/RecordTests.scala]: ../../test/scala/RecordTests.scala.md
[test/scala/TypeSetTests.scala]: ../../test/scala/TypeSetTests.scala.md