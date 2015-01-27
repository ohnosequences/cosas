## Conversions to HList and List

```scala
package ohnosequences.cosas.ops.typeSets

import ohnosequences.cosas._, fns._, typeSets._, types._
import shapeless._, poly._

case object id extends Poly1 { implicit def default[T] = at[T]((t:T) => t) }


@annotation.implicitNotFound(msg = "Can't convert ${S} to an HList")
trait ToHList[S <: AnyTypeSet] extends Fn1[S] with OutBound[HList]

object ToHList {

  def apply[S <: AnyTypeSet](implicit toHList: ToHList[S]): ToHList[S] = toHList

  implicit def any[S <: AnyTypeSet, O <: HList]
    (implicit 
      mapper: MapToHList[id.type, S] { type Out = O }
    ):  ToHList[S] with Out[O] =
    new ToHList[S] with Out[O] {
      def apply(s: S): Out = mapper(s)
    }

}


@annotation.implicitNotFound(msg = "Can't convert ${L} to a TypeSet (maybe element types are not distinct)")
trait FromHList[L <: HList] extends Fn1[L] with OutBound[AnyTypeSet]

object FromHList {

  def apply[L <: HList](implicit fromHList: FromHList[L]): FromHList[L] = fromHList

  implicit def hnil[N <: HNil]: 
        FromHList[N] with Out[∅] = 
    new FromHList[N] with Out[∅] {
      def apply(l: N): Out = ∅
    }
  
  implicit def cons[H, T <: HList, OutT <: AnyTypeSet]
    (implicit 
      rest: FromHList[T] { type Out = OutT },
      check: H ∉ OutT
    ):  FromHList[H :: T] with Out[H :~: OutT] = 
    new FromHList[H :: T] with Out[H :~: OutT] {
      def apply(l: H :: T): Out = l.head :~: rest(l.tail)
    }

}


@annotation.implicitNotFound(msg = "Can't convert ${S} to a List")
trait ToList[S <: AnyTypeSet] extends Fn1[S] with OutInContainer[List]

object ToList {

  def apply[S <: AnyTypeSet](implicit toList: ToList[S]): ToList[S] = toList

  // case object id_ extends Poly1 { implicit def default[T <: X, X] = at[T]{ (t: T) => (t: X) } }
  
  // implicit def any[S <: AnyTypeSet, O](implicit 
  //     mapper: (id_.type MapToList S) with wrapped[O]
  //   ):  ToList[S] with InContainer[O] =
  //   new ToList[S] with InContainer[O] { def apply(s: S): Out = mapper(s) }

  implicit def empty[X]: 
        ToList[∅] with InContainer[X] = 
    new ToList[∅] with InContainer[X] { def apply(s: ∅): Out = Nil }
  
  implicit def one[X, H <: X]:
        ToList[H :~: ∅] with InContainer[X] =
    new ToList[H :~: ∅] with InContainer[X] { def apply(s: H :~: ∅): Out = List[X](s.head) }

  implicit def cons2[X, H1 <: X, H2 <: X, T <: AnyTypeSet]
    (implicit 
      lt: ToList[H2 :~: T] { type O = X }
    ):  ToList[H1 :~: H2 :~: T] with InContainer[X] = 
    new ToList[H1 :~: H2 :~: T] with InContainer[X] {

      def apply(s: H1 :~: H2 :~: T): Out = s.head :: lt(s.tail.head :~: s.tail.tail)
    }

}


@annotation.implicitNotFound(msg = "Can't parse typeset ${S} from ${X}")
// NOTE: it should be restricted to AnyTypeSet.Of[AnyType], when :~: is known to return the same thing
trait ParseFrom[S <: AnyTypeSet, X] extends Fn2[S, X] with OutBound[AnyTypeSet]

object ParseFrom {

  def apply[S <: AnyTypeSet, X]
    (implicit parser: ParseFrom[S, X]): ParseFrom[S, X] = parser

  implicit def empty[X]: 
        (∅ ParseFrom X) with Out[∅] = 
    new (∅ ParseFrom X) with Out[∅] {

      def apply(s: ∅, x: X): Out = ∅
    }

  implicit def cons[X,
    H <: AnyType, T <: AnyTypeSet, TO <: AnyTypeSet
  ](implicit
    f: (H, X) => (ValueOf[H], X),
    t: ParseFrom[T, X] { type Out = TO }
  ):  ((H :~: T) ParseFrom X) with Out[ValueOf[H] :~: TO] =
  new ((H :~: T) ParseFrom X) with Out[ValueOf[H] :~: TO] {

    def apply(s: H :~: T, x: X): Out = {
      val (head, rest) = f(s.head, x)
      head :~: t(s.tail, rest)
    }
  }
}


// trait AnyMonoid {
//   type M
//   def zero: M
//   def append(a: M, b: M): M
// }

// trait Monoid[T] extends AnyMonoid { type M = T }

import spire.algebra.Monoid

@annotation.implicitNotFound(msg = "Can't serialize typeset ${S} to ${X}")
trait SerializeTo[S <: AnyTypeSet, X] extends Fn1[S] with Out[X]

object SerializeTo {

  def apply[S <: AnyTypeSet, X]
    (implicit serializer: S SerializeTo X): S SerializeTo X = serializer

  implicit def empty[X](implicit m: Monoid[X]):
        (∅ SerializeTo X) = 
    new (∅ SerializeTo X) {

      def apply(r: ∅): Out = m.id
    }

  implicit def cons[X,
    H, T <: AnyTypeSet
  ](implicit
    m: Monoid[X],
    f: H => X,
    t: T SerializeTo X
  ):  ((H :~: T) SerializeTo X) =
  new ((H :~: T) SerializeTo X) {
    
    def apply(s: H :~: T): Out = m.op(f(s.head), t(s.tail))
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