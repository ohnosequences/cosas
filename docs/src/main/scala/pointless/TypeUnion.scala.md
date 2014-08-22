
```scala
package ohnosequences.pointless

import shapeless.{ <:!< }

object typeUnion {
```


The two type-level constructors of a type union. 
A generic term looks like `either[A]#or[B]#or[C]`.


```scala
  final type either[X] = TypeUnion[not[X]]

  trait AnyTypeUnion {

    type or[Y] <: AnyTypeUnion
    type union // kind of return
  }
```

Builder

```scala
  trait TypeUnion[T] extends AnyTypeUnion {

    type or[S] = TypeUnion[T with not[S]]  
    type union = not[T]
  }

  private type not[T] = T => Nothing
  private type just[T] = not[not[T]]
```


Type-level operations


```scala
  @annotation.implicitNotFound(msg = "isOneOf check is not implemented")
  type    isOneOf[X, U <: AnyTypeUnion] = just[X] <:<  U#union

  @annotation.implicitNotFound(msg = "isNotOneOf check is not implemented")
  type isNotOneOf[X, U <: AnyTypeUnion] = just[X] <:!< U#union

  final type oneOf[U <: AnyTypeUnion] = {
    type    is[X] = X    isOneOf U
    type isNot[X] = X isNotOneOf U
  }
}

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