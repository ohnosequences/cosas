/* ## Conversions to HList and List */

package ohnosequences.pointless.impl.ops

import ohnosequences.pointless._, AnyFn._, impl._, typeSet._
import shapeless._

case object id extends Poly1 { implicit def default[T] = at[T]((t:T) => t) }


@annotation.implicitNotFound(msg = "Can't convert ${S} to an HList")
trait ToHList[S <: AnyTypeSet] extends Fn1[S] with WithCodomain[HList]

object ToHList {

  def apply[S <: AnyTypeSet](implicit toHList: ToHList[S]): ToHList[S] with out[toHList.Out] = toHList

  implicit def any[S <: AnyTypeSet](implicit 
      mapper: id.type MapToHList S
    ):  ToHList[S] with out[mapper.Out] =
    new ToHList[S] {
      type Out = mapper.Out
      def apply(s: S): Out = mapper(s)
    }

}

// TODO: FromHList


@annotation.implicitNotFound(msg = "Can't convert ${S} to a List")
trait ToList[S <: AnyTypeSet] extends Fn1[S] with WrappedIn[List]

object ToList {

  def apply[S <: AnyTypeSet](implicit toList: ToList[S]): ToList[S] with out[toList.Out] = toList

  // NOTE: approach with using MapToList doesn't work...
  
  // case object id_ extends Poly1 { implicit def default[T <: X, X] = at[T]((t: T) => (t: X)) }
  // 
  // implicit def any[S <: AnyTypeSet](implicit 
  //     mapper: id_.type MapToList S
  //   ):  ToList[S] with o[mapper.O] =
  //   new ToList[S] {
  //     type O = mapper.O
  //     def apply(s: S): Out = mapper(s)
  //   }

  implicit def emptyToList[X]: ToList[∅] with o[X] = 
    new ToList[∅] {
      type O = X
      def apply(s: ∅): Out = Nil
    }
  
  implicit def oneToList[X, H <: X]: ToList[H :~: ∅] with o[X] =
    new ToList[H :~: ∅] {
      type O = X
      def apply(s: H :~: ∅): Out = List[X](s.head)
    }

  implicit def cons2ToList[X, H1 <: X, H2 <: X, T <: AnyTypeSet]
    (implicit 
      lt: ToList[H2 :~: T] with o[X]
    ):  ToList[H1 :~: H2 :~: T] with o[X] = 
    new ToList[H1 :~: H2 :~: T] {
      type O = X
      def apply(s: H1 :~: H2 :~: T): Out = s.head :: lt(s.tail.head :~: s.tail.tail)
    }
}
