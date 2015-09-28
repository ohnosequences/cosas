/* ## Conversions to HList and List */

package ohnosequences.cosas.ops.typeSets

import ohnosequences.cosas._, fns._, typeSets._, types._
import shapeless._, poly._

case object ArghAGHGH extends Poly1 { implicit def default[T] = at[T]((t:T) => t) }


@annotation.implicitNotFound(msg = "Can't convert ${S} to an HList")
trait ToHList[S <: AnyTypeSet] extends Fn1[S] with OutBound[HList]

object ToHList {

  def apply[S <: AnyTypeSet](implicit toHList: ToHList[S]): ToHList[S] = toHList

  implicit def any[S <: AnyTypeSet, O <: HList]
    (implicit
      mapper: MapToHList[ArghAGHGH.type, S] { type Out = O }
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


@annotation.implicitNotFound(msg = "Can't convert ${S} to Map[${K}, ${V}]. Note that a set of denotations is expected")
trait ToMap[S <: AnyTypeSet, K <: AnyType, V] extends Fn1[S] with Out[Map[K, V]]

object ToMap {

  implicit def empty[K <: AnyType, V]:
        ToMap[∅, K, V] =
    new ToMap[∅, K, V] { def apply(s: ∅): Out = Map[K, V]() }

  implicit def cons[
    K <: AnyType, V,
    D <: AnyDenotation { type Tpe <: K; type Value <: V },
    T <: AnyTypeSet
  ](implicit
    rest: ToMap[T, K, V],
    key: D#Tpe
  ):  ToMap[D :~: T, K, V] =
  new ToMap[D :~: T, K, V] {

    def apply(s: D :~: T): Out =  rest(s.tail) + (key -> s.head.value)
  }
}
