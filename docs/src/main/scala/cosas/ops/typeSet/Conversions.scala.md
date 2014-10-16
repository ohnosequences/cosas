## Conversions to HList and List

```scala
package ohnosequences.cosas.ops.typeSet

import ohnosequences.cosas._, AnyFn._, AnyTypeSet._, AnyWrap._
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
// NOTE: it should be restricted to AnyTypeSet.Of[AnyWrap], when :~: is known to return the same thing
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
    H <: AnyWrap, T <: AnyTypeSet, TO <: AnyTypeSet
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


trait AnyMonoid {
  type M
  def zero: M
  def append(a: M, b: M): M
}

trait Monoid[T] extends AnyMonoid { type M = T }


@annotation.implicitNotFound(msg = "Can't serialize typeset ${S} to ${X}")
trait SerializeTo[S <: AnyTypeSet, X] extends Fn1[S] with Out[X]

object SerializeTo {

  def apply[S <: AnyTypeSet, X]
    (implicit serializer: S SerializeTo X): S SerializeTo X = serializer

  implicit def empty[X](implicit m: Monoid[X]):
        (∅ SerializeTo X) = 
    new (∅ SerializeTo X) {

      def apply(r: ∅): Out = m.zero
    }

  implicit def cons[X,
    H, T <: AnyTypeSet
  ](implicit
    m: Monoid[X],
    f: H => X,
    t: T SerializeTo X
  ):  ((H :~: T) SerializeTo X) =
  new ((H :~: T) SerializeTo X) {
    
    def apply(s: H :~: T): Out = m.append(f(s.head), t(s.tail))
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