
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

@annotation.implicitNotFound(msg = "Can't get types of the denotations set ${S}")
trait TypesOf[S <: AnyTypeSet] extends Fn1[S] with OutBound[AnyTypeSet]

object TypesOf {

  implicit val empty:
        TypesOf[∅] with Out[∅] =
    new TypesOf[∅] with Out[∅] { def apply(s: ∅): Out = ∅ }

  implicit def cons[D <: AnyDenotation, H <: D#Tpe, T <: AnyTypeSet, TO <: AnyTypeSet]
    (implicit
      getH: H,
      rest: TypesOf[T] { type Out = TO }
    ):  TypesOf[D :~: T] with Out[H :~: TO] =
    new TypesOf[D :~: T] with Out[H :~: TO] {

      def apply(s: D :~: T): Out = getH :~: rest(s.tail)
    }
}

```




[test/scala/cosas/asserts.scala]: ../../../../../test/scala/cosas/asserts.scala.md
[test/scala/cosas/DenotationTests.scala]: ../../../../../test/scala/cosas/DenotationTests.scala.md
[test/scala/cosas/SubsetTypesTests.scala]: ../../../../../test/scala/cosas/SubsetTypesTests.scala.md
[test/scala/cosas/EqualityTests.scala]: ../../../../../test/scala/cosas/EqualityTests.scala.md
[test/scala/cosas/PropertyTests.scala]: ../../../../../test/scala/cosas/PropertyTests.scala.md
[test/scala/cosas/RecordTests.scala]: ../../../../../test/scala/cosas/RecordTests.scala.md
[test/scala/cosas/TypeSetTests.scala]: ../../../../../test/scala/cosas/TypeSetTests.scala.md
[test/scala/cosas/TypeUnionTests.scala]: ../../../../../test/scala/cosas/TypeUnionTests.scala.md
[main/scala/cosas/typeUnions.scala]: ../../typeUnions.scala.md
[main/scala/cosas/properties.scala]: ../../properties.scala.md
[main/scala/cosas/records.scala]: ../../records.scala.md
[main/scala/cosas/fns.scala]: ../../fns.scala.md
[main/scala/cosas/types.scala]: ../../types.scala.md
[main/scala/cosas/typeSets.scala]: ../../typeSets.scala.md
[main/scala/cosas/ops/records/Update.scala]: ../records/Update.scala.md
[main/scala/cosas/ops/records/Transform.scala]: ../records/Transform.scala.md
[main/scala/cosas/ops/records/Get.scala]: ../records/Get.scala.md
[main/scala/cosas/ops/typeSets/SerializeDenotations.scala]: SerializeDenotations.scala.md
[main/scala/cosas/ops/typeSets/ParseDenotations.scala]: ParseDenotations.scala.md
[main/scala/cosas/ops/typeSets/Conversions.scala]: Conversions.scala.md
[main/scala/cosas/ops/typeSets/Filter.scala]: Filter.scala.md
[main/scala/cosas/ops/typeSets/Subtract.scala]: Subtract.scala.md
[main/scala/cosas/ops/typeSets/Mappers.scala]: Mappers.scala.md
[main/scala/cosas/ops/typeSets/Union.scala]: Union.scala.md
[main/scala/cosas/ops/typeSets/Reorder.scala]: Reorder.scala.md
[main/scala/cosas/ops/typeSets/Take.scala]: Take.scala.md
[main/scala/cosas/ops/typeSets/Representations.scala]: Representations.scala.md
[main/scala/cosas/ops/typeSets/Pop.scala]: Pop.scala.md
[main/scala/cosas/ops/typeSets/Replace.scala]: Replace.scala.md
[main/scala/cosas/equality.scala]: ../../equality.scala.md