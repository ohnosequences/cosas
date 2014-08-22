
```scala
package ohnosequences.pointless
```


Tagging is used for being able to operate on `Raw` values knowing what they are representing; `Rep` is just `Raw` tagged with the `.type` of this representation. So, summarizing

- `Raw` is the type used to the represent `this.type`
- `Rep <: Raw` is just `Raw` tagged with `this.type`; the `Rep`resentation


```scala
object taggedType {

  trait AnyTaggedType { me => 

    final type Me = me.type

    type Raw
```


`Raw` tagged with `self.type`; this lets you recognize a denotation while being able to operate on it as `Raw`.


```scala
    // we don't actually need this
    // final type Rep = Tagged[Me]

    implicit def fromTagged(x: Tagged[Me]): Me = me
    // I don't know why this works
    implicit def yetAnotherFromRep[X <: Me](rep: Tagged[X]): Me = me
  }

  trait TaggedType[R] extends AnyTaggedType {

    type Raw = R
  }

  type Tagged[T <: AnyTaggedType] = T#Raw with Tag[T]
  // FIXME: "Not a simple type"
  type RawOf[T <: AnyTaggedType] = T#Raw
  
  type @@[T <: AnyTaggedType] = Tagged[T]

  implicit def taggedTypeOps[T <: AnyTaggedType](t: T): TaggedTypeOps[T] = new TaggedTypeOps[T](t)
  
  class TaggedTypeOps[T <: AnyTaggedType](val t: T) {

    def =>>[R <: RawOf[T]](raw: R): Tagged[T] = TagWith[T](t)(raw)
  }
```


Tagging


```scala
  case class TagWith[T <: AnyTaggedType](val t: T) {

    def apply(r: RawOf[T]): Tagged[T] = r.asInstanceOf[Tagged[T]]
  }

  // Has to be empty! See http://www.scala-lang.org/old/node/11165.html#comment-49097
  sealed trait AnyTag {}
  sealed trait Tag[T <: AnyTaggedType] extends AnyTag with shapeless.record.KeyTag[T, RawOf[T]]

  // def tagWith[R <: AnyTaggedType, U <: R#Raw](r: R, raw: U): Tagged[R] = TagWith[R](r)(raw)

}

  // /*
  //   This trait represents a mapping between 

  //   - members `Tpe` of a universe of types `TYPE`
  //   - and `Raw` a type meant to be a denotation of `Tpe` thus the name

  // */
  // trait AnyDenotation extends Representable {

  //   /* The base type for the types that this thing denotes */
  //   type TYPE
  //   type Tpe <: TYPE
  //   val  tpe: Tpe
  // }

  // /*
  //   Bound the universe of types to be `T`s
  // */
  // trait Denotation[T] extends AnyDenotation { type TYPE = T }

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

[test/scala/pointless/PropertyTests.scala]: ../../../test/scala/pointless/PropertyTests.scala.md
[test/scala/pointless/RecordTests.scala]: ../../../test/scala/pointless/RecordTests.scala.md
[test/scala/pointless/TypeSetTests.scala]: ../../../test/scala/pointless/TypeSetTests.scala.md
[main/scala/pointless/TaggedType.scala]: TaggedType.scala.md
[main/scala/pointless/Record.scala]: Record.scala.md
[main/scala/pointless/ops/typeSet/Lookup.scala]: ops/typeSet/Lookup.scala.md
[main/scala/pointless/ops/typeSet/Reorder.scala]: ops/typeSet/Reorder.scala.md
[main/scala/pointless/ops/typeSet/Conversions.scala]: ops/typeSet/Conversions.scala.md
[main/scala/pointless/ops/typeSet/Subtract.scala]: ops/typeSet/Subtract.scala.md
[main/scala/pointless/ops/typeSet/Pop.scala]: ops/typeSet/Pop.scala.md
[main/scala/pointless/ops/typeSet/Representations.scala]: ops/typeSet/Representations.scala.md
[main/scala/pointless/ops/typeSet/Replace.scala]: ops/typeSet/Replace.scala.md
[main/scala/pointless/ops/typeSet/Take.scala]: ops/typeSet/Take.scala.md
[main/scala/pointless/ops/typeSet/Union.scala]: ops/typeSet/Union.scala.md
[main/scala/pointless/ops/typeSet/Mappers.scala]: ops/typeSet/Mappers.scala.md
[main/scala/pointless/ops/record/Update.scala]: ops/record/Update.scala.md
[main/scala/pointless/ops/record/Conversions.scala]: ops/record/Conversions.scala.md
[main/scala/pointless/ops/record/Get.scala]: ops/record/Get.scala.md
[main/scala/pointless/Denotation.scala]: Denotation.scala.md
[main/scala/pointless/TypeUnion.scala]: TypeUnion.scala.md
[main/scala/pointless/Fn.scala]: Fn.scala.md
[main/scala/pointless/Property.scala]: Property.scala.md
[main/scala/pointless/TypeSet.scala]: TypeSet.scala.md