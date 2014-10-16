
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
package ohnosequences.cosas.ops.typeSet

import ohnosequences.cosas._, AnyFn._, AnyWrap._, AnyTypeSet._

@annotation.implicitNotFound(msg = "Can't construct a set of values for ${S}")
trait ValuesOf[S <: AnyTypeSet] extends AnyFn with OutBound[AnyTypeSet]

object ValuesOf {

  implicit val empty: 
        ValuesOf[∅] with Out[∅] = 
    new ValuesOf[∅] with Out[∅]

  implicit def cons[H <: AnyWrap, T <: AnyTypeSet, TR <: AnyTypeSet]
    (implicit 
      t: ValuesOf[T] { type Out = TR }
    ):  ValuesOf[H :~: T] with Out[ValueOf[H] :~: TR] =
    new ValuesOf[H :~: T] with Out[ValueOf[H] :~: TR]
}

@annotation.implicitNotFound(msg = "Can't construct a set of raw types for ${S}")
trait UnionOfRaws[S <: AnyTypeSet] extends AnyFn with OutBound[AnyTypeUnion]

object UnionOfRaws {

  implicit val empty: UnionOfRaws[∅] with Out[TypeUnion.empty] =
                  new UnionOfRaws[∅] with Out[TypeUnion.empty]

  implicit def cons[H <: AnyWrap, T <: AnyTypeSet, TU <: AnyTypeUnion]
    (implicit 
      t: UnionOfRaws[T] { type Out = TU }
    ):  UnionOfRaws[H :~: T] with Out[TU#or[RawOf[H]]] =
    new UnionOfRaws[H :~: T] with Out[TU#or[RawOf[H]]]
}

@annotation.implicitNotFound(msg = "Can't get wraps of the values set ${S}")
trait WrapsOf[S <: AnyTypeSet] extends Fn1[S] with OutBound[AnyTypeSet]

object WrapsOf {

  implicit val empty: 
        WrapsOf[∅] with Out[∅] =
    new WrapsOf[∅] with Out[∅] { def apply(s: ∅): Out = ∅ }

  implicit def cons[H <: AnyWrap, T <: AnyTypeSet, TO <: AnyTypeSet]
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
        + [PropertyTests.scala][test/scala/cosas/PropertyTests.scala]
        + [WrapTests.scala][test/scala/cosas/WrapTests.scala]
        + [RecordTests.scala][test/scala/cosas/RecordTests.scala]
        + [TypeSetTests.scala][test/scala/cosas/TypeSetTests.scala]
  + main
    + scala
      + cosas
        + [Wrap.scala][main/scala/cosas/Wrap.scala]
        + [PropertiesHolder.scala][main/scala/cosas/PropertiesHolder.scala]
        + [Record.scala][main/scala/cosas/Record.scala]
        + ops
          + typeSet
            + [Check.scala][main/scala/cosas/ops/typeSet/Check.scala]
            + [Reorder.scala][main/scala/cosas/ops/typeSet/Reorder.scala]
            + [Conversions.scala][main/scala/cosas/ops/typeSet/Conversions.scala]
            + [AggregateProperties.scala][main/scala/cosas/ops/typeSet/AggregateProperties.scala]
            + [Subtract.scala][main/scala/cosas/ops/typeSet/Subtract.scala]
            + [Pop.scala][main/scala/cosas/ops/typeSet/Pop.scala]
            + [Representations.scala][main/scala/cosas/ops/typeSet/Representations.scala]
            + [Replace.scala][main/scala/cosas/ops/typeSet/Replace.scala]
            + [Take.scala][main/scala/cosas/ops/typeSet/Take.scala]
            + [Union.scala][main/scala/cosas/ops/typeSet/Union.scala]
            + [Mappers.scala][main/scala/cosas/ops/typeSet/Mappers.scala]
          + record
            + [Update.scala][main/scala/cosas/ops/record/Update.scala]
            + [Conversions.scala][main/scala/cosas/ops/record/Conversions.scala]
            + [Get.scala][main/scala/cosas/ops/record/Get.scala]
        + [Denotation.scala][main/scala/cosas/Denotation.scala]
        + [TypeUnion.scala][main/scala/cosas/TypeUnion.scala]
        + [Fn.scala][main/scala/cosas/Fn.scala]
        + [Property.scala][main/scala/cosas/Property.scala]
        + [TypeSet.scala][main/scala/cosas/TypeSet.scala]

[test/scala/cosas/PropertyTests.scala]: ../../../../../test/scala/cosas/PropertyTests.scala.md
[test/scala/cosas/WrapTests.scala]: ../../../../../test/scala/cosas/WrapTests.scala.md
[test/scala/cosas/RecordTests.scala]: ../../../../../test/scala/cosas/RecordTests.scala.md
[test/scala/cosas/TypeSetTests.scala]: ../../../../../test/scala/cosas/TypeSetTests.scala.md
[main/scala/cosas/Wrap.scala]: ../../Wrap.scala.md
[main/scala/cosas/PropertiesHolder.scala]: ../../PropertiesHolder.scala.md
[main/scala/cosas/Record.scala]: ../../Record.scala.md
[main/scala/cosas/ops/typeSet/Check.scala]: Check.scala.md
[main/scala/cosas/ops/typeSet/Reorder.scala]: Reorder.scala.md
[main/scala/cosas/ops/typeSet/Conversions.scala]: Conversions.scala.md
[main/scala/cosas/ops/typeSet/AggregateProperties.scala]: AggregateProperties.scala.md
[main/scala/cosas/ops/typeSet/Subtract.scala]: Subtract.scala.md
[main/scala/cosas/ops/typeSet/Pop.scala]: Pop.scala.md
[main/scala/cosas/ops/typeSet/Representations.scala]: Representations.scala.md
[main/scala/cosas/ops/typeSet/Replace.scala]: Replace.scala.md
[main/scala/cosas/ops/typeSet/Take.scala]: Take.scala.md
[main/scala/cosas/ops/typeSet/Union.scala]: Union.scala.md
[main/scala/cosas/ops/typeSet/Mappers.scala]: Mappers.scala.md
[main/scala/cosas/ops/record/Update.scala]: ../record/Update.scala.md
[main/scala/cosas/ops/record/Conversions.scala]: ../record/Conversions.scala.md
[main/scala/cosas/ops/record/Get.scala]: ../record/Get.scala.md
[main/scala/cosas/Denotation.scala]: ../../Denotation.scala.md
[main/scala/cosas/TypeUnion.scala]: ../../TypeUnion.scala.md
[main/scala/cosas/Fn.scala]: ../../Fn.scala.md
[main/scala/cosas/Property.scala]: ../../Property.scala.md
[main/scala/cosas/TypeSet.scala]: ../../TypeSet.scala.md