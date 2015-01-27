
## Building a set of representations

This is a generic thing for deriving the set of representations 
from a set of taggedType singletons. For example:
```scala
case object id extends Property[Int]
case object name extends Property[String]

implicitly[ValuesOf.By[
  id.type :~: name.type :~: ∅,
  id.Rep  :~: name.Rep  :~: ∅
]]
```

See examples of usage it for record properties in tests


```scala
package ohnosequences.cosas.ops.typeSets

import ohnosequences.cosas._, fns._, types._, typeUnions._, typeSets._

@annotation.implicitNotFound(msg = "Can't construct a set of values for ${S}")
trait ValuesOf[S <: AnyTypeSet] extends AnyFn with OutBound[AnyTypeSet]

object ValuesOf {

  implicit val empty: 
        ValuesOf[∅] with Out[∅] = 
    new ValuesOf[∅] with Out[∅]

  implicit def cons[H <: AnyType, T <: AnyTypeSet, TR <: AnyTypeSet]
    (implicit 
      t: ValuesOf[T] { type Out = TR }
    ):  ValuesOf[H :~: T] with Out[ValueOf[H] :~: TR] =
    new ValuesOf[H :~: T] with Out[ValueOf[H] :~: TR]
}

@annotation.implicitNotFound(msg = "Can't construct a set of raw types for ${S}")
trait UnionOfRaws[S <: AnyTypeSet] extends AnyFn with OutBound[AnyTypeUnion]

object UnionOfRaws {

  implicit val empty: UnionOfRaws[∅] with Out[empty] =
                  new UnionOfRaws[∅] with Out[empty]

  implicit def cons[H <: AnyType, T <: AnyTypeSet, TU <: AnyTypeUnion]
    (implicit 
      t: UnionOfRaws[T] { type Out = TU }
    ):  UnionOfRaws[H :~: T] with Out[TU#or[H#Raw]] =
    new UnionOfRaws[H :~: T] with Out[TU#or[H#Raw]]
}

@annotation.implicitNotFound(msg = "Can't get wraps of the values set ${S}")
trait WrapsOf[S <: AnyTypeSet] extends Fn1[S] with OutBound[AnyTypeSet]

object WrapsOf {

  implicit val empty: 
        WrapsOf[∅] with Out[∅] =
    new WrapsOf[∅] with Out[∅] { def apply(s: ∅): Out = ∅ }

  implicit def cons[H <: AnyType, T <: AnyTypeSet, TO <: AnyTypeSet]
    (implicit 
      getH: ValueOf[H] => H, 
      rest: WrapsOf[T] { type Out = TO }
    ):  WrapsOf[ValueOf[H] :~: T] with Out[H :~: TO] =
    new WrapsOf[ValueOf[H] :~: T] with Out[H :~: TO] {

      def apply(s: ValueOf[H] :~: T): Out = getH(s.head) :~: rest(s.tail)
    }
}

```


------

### Index

+ src
  + test
    + scala
      + cosas
        + [SubsetTypesTests.scala][test/scala/cosas/SubsetTypesTests.scala]
        + [PropertyTests.scala][test/scala/cosas/PropertyTests.scala]
        + [TypeUnionTests.scala][test/scala/cosas/TypeUnionTests.scala]
        + [ScalazEquality.scala][test/scala/cosas/ScalazEquality.scala]
        + [EqualityTests.scala][test/scala/cosas/EqualityTests.scala]
        + [DenotationTests.scala][test/scala/cosas/DenotationTests.scala]
        + [RecordTests.scala][test/scala/cosas/RecordTests.scala]
        + [TypeSetTests.scala][test/scala/cosas/TypeSetTests.scala]
  + main
    + scala
      + cosas
        + [equality.scala][main/scala/cosas/equality.scala]
        + [properties.scala][main/scala/cosas/properties.scala]
        + [typeSets.scala][main/scala/cosas/typeSets.scala]
        + ops
          + records
            + [Update.scala][main/scala/cosas/ops/records/Update.scala]
            + [Conversions.scala][main/scala/cosas/ops/records/Conversions.scala]
            + [Get.scala][main/scala/cosas/ops/records/Get.scala]
          + typeSets
            + [Filter.scala][main/scala/cosas/ops/typeSets/Filter.scala]
            + [Reorder.scala][main/scala/cosas/ops/typeSets/Reorder.scala]
            + [Conversions.scala][main/scala/cosas/ops/typeSets/Conversions.scala]
            + [AggregateProperties.scala][main/scala/cosas/ops/typeSets/AggregateProperties.scala]
            + [Subtract.scala][main/scala/cosas/ops/typeSets/Subtract.scala]
            + [Pop.scala][main/scala/cosas/ops/typeSets/Pop.scala]
            + [Representations.scala][main/scala/cosas/ops/typeSets/Representations.scala]
            + [Replace.scala][main/scala/cosas/ops/typeSets/Replace.scala]
            + [Take.scala][main/scala/cosas/ops/typeSets/Take.scala]
            + [Union.scala][main/scala/cosas/ops/typeSets/Union.scala]
            + [Mappers.scala][main/scala/cosas/ops/typeSets/Mappers.scala]
        + [typeUnions.scala][main/scala/cosas/typeUnions.scala]
        + [records.scala][main/scala/cosas/records.scala]
        + [subsetTypes.scala][main/scala/cosas/subsetTypes.scala]
        + [fns.scala][main/scala/cosas/fns.scala]
        + [propertyHolders.scala][main/scala/cosas/propertyHolders.scala]
        + [types.scala][main/scala/cosas/types.scala]

[test/scala/cosas/SubsetTypesTests.scala]: ../../../../../test/scala/cosas/SubsetTypesTests.scala.md
[test/scala/cosas/PropertyTests.scala]: ../../../../../test/scala/cosas/PropertyTests.scala.md
[test/scala/cosas/TypeUnionTests.scala]: ../../../../../test/scala/cosas/TypeUnionTests.scala.md
[test/scala/cosas/ScalazEquality.scala]: ../../../../../test/scala/cosas/ScalazEquality.scala.md
[test/scala/cosas/EqualityTests.scala]: ../../../../../test/scala/cosas/EqualityTests.scala.md
[test/scala/cosas/DenotationTests.scala]: ../../../../../test/scala/cosas/DenotationTests.scala.md
[test/scala/cosas/RecordTests.scala]: ../../../../../test/scala/cosas/RecordTests.scala.md
[test/scala/cosas/TypeSetTests.scala]: ../../../../../test/scala/cosas/TypeSetTests.scala.md
[main/scala/cosas/equality.scala]: ../../equality.scala.md
[main/scala/cosas/properties.scala]: ../../properties.scala.md
[main/scala/cosas/typeSets.scala]: ../../typeSets.scala.md
[main/scala/cosas/ops/records/Update.scala]: ../records/Update.scala.md
[main/scala/cosas/ops/records/Conversions.scala]: ../records/Conversions.scala.md
[main/scala/cosas/ops/records/Get.scala]: ../records/Get.scala.md
[main/scala/cosas/ops/typeSets/Filter.scala]: Filter.scala.md
[main/scala/cosas/ops/typeSets/Reorder.scala]: Reorder.scala.md
[main/scala/cosas/ops/typeSets/Conversions.scala]: Conversions.scala.md
[main/scala/cosas/ops/typeSets/AggregateProperties.scala]: AggregateProperties.scala.md
[main/scala/cosas/ops/typeSets/Subtract.scala]: Subtract.scala.md
[main/scala/cosas/ops/typeSets/Pop.scala]: Pop.scala.md
[main/scala/cosas/ops/typeSets/Representations.scala]: Representations.scala.md
[main/scala/cosas/ops/typeSets/Replace.scala]: Replace.scala.md
[main/scala/cosas/ops/typeSets/Take.scala]: Take.scala.md
[main/scala/cosas/ops/typeSets/Union.scala]: Union.scala.md
[main/scala/cosas/ops/typeSets/Mappers.scala]: Mappers.scala.md
[main/scala/cosas/typeUnions.scala]: ../../typeUnions.scala.md
[main/scala/cosas/records.scala]: ../../records.scala.md
[main/scala/cosas/subsetTypes.scala]: ../../subsetTypes.scala.md
[main/scala/cosas/fns.scala]: ../../fns.scala.md
[main/scala/cosas/propertyHolders.scala]: ../../propertyHolders.scala.md
[main/scala/cosas/types.scala]: ../../types.scala.md