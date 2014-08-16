package ohnosequences.pointless.impl.ops

import ohnosequences.pointless._, AnyFn._, impl._, typeSet._
import shapeless._, poly._
  

/* Mapping a set to an HList: when you want to preserve precise types, but they are not distinct */
@annotation.implicitNotFound(msg = "Can't map ${F} over ${S} to an HList")
trait MapToHList[F <: Poly1, S <: AnyTypeSet] extends Fn1[S] with WithCodomain[HList]

object MapToHList {

  def apply[F <: Poly1, S <: AnyTypeSet]
    (implicit mapper: MapToHList[F, S]): MapToHList[F, S] with out[mapper.Out] = mapper

  implicit def empty[F <: Poly1]: MapToHList[F, ∅] with out[HNil] =
    new MapToHList[F, ∅] {
      type Out = HNil
      def apply(s: ∅): Out = HNil
    }
  
  implicit def cons[
    F <: Poly1, 
    H, T <: AnyTypeSet, 
    OutH, OutT <: HList
  ](implicit
    h: Case1.Aux[F, H, OutH], 
    t: MapToHList[F, T] with out[OutT]
  ):  MapToHList[F, H :~: T] with out[OutH :: OutT] = 
  new MapToHList[F, H :~: T] { 
    type Out = OutH :: OutT
    def apply(s: H :~: T): Out = h(s.head) :: t(s.tail:T)
  }
}

/* Mapping a set to a List: normally, when you are mapping everything to one type */
@annotation.implicitNotFound(msg = "Can't map ${F} over ${S} to a List")
trait MapToList[F <: Poly1, S <: AnyTypeSet] extends Fn1[S] with WrappedIn[List] 

object MapToList {

  def apply[F <: Poly1, S <: AnyTypeSet]
    (implicit mapper: MapToList[F, S]): MapToList[F, S] with o[mapper.O] = mapper
  
  implicit def empty[F <: Poly1, X]: MapToList[F, ∅] with o[X] = 
    new MapToList[F, ∅] { 
      type O = X
      def apply(s: ∅): Out = Nil
    }
  
  implicit def one[H, F <: Poly1, X]
    (implicit h: Case1.Aux[F, H, X]): MapToList[F, H :~: ∅] with o[X] = 
      new MapToList[F, H :~: ∅] {
        type O = X
        def apply(s: H :~: ∅): Out = List[X](h(s.head))
      }

  implicit def cons2[H1, H2, T <: AnyTypeSet, F <: Poly1, X]
    (implicit
      h: Case1.Aux[F, H1, X], 
      t: MapToList[F, H2 :~: T] with o[X]
    ):  MapToList[F, H1 :~: H2 :~: T] with o[X] = 
    new MapToList[F, H1 :~: H2 :~: T] { 
      type O = X
      def apply(s: H1 :~: H2 :~: T): Out = h(s.head) :: t(s.tail)
    }
}

/* Mapping a set to another set, i.e. the results of mapping should have distinct types */  
@annotation.implicitNotFound(msg = "Can't map ${F} over ${S} (maybe the resulting types are not distinct)")
trait MapSet[F <: Poly1, S <: AnyTypeSet] extends Fn1[S] with WithCodomain[AnyTypeSet]

object MapSet {

  def apply[F <: Poly1, S <: AnyTypeSet]
    (implicit mapper: MapSet[F, S]): MapSet[F, S] with out[mapper.Out] = mapper

  implicit def empty[F <: Poly1]: MapSet[F, ∅] with out[∅] = 
    new MapSet[F, ∅] {
      type Out = ∅
      def apply(s: ∅): Out = ∅
    }
  
  implicit def cons[
    F <: Poly1,
    H, OutH,
    T <: AnyTypeSet, OutT <: AnyTypeSet
  ](implicit
    h: Case1.Aux[F, H, OutH], 
    t: MapSet[F, T] with out[OutT],
    e: OutH ∉ OutT  // the key check here
  ):  MapSet[F, H :~: T] with out[OutH :~: OutT] = 
  new MapSet[F, H :~: T] {
    type Out = OutH :~: OutT
    def apply(s: H :~: T): Out = h(s.head) :~: t(s.tail)
  }
}

/*
Map-folder for sets 
    
Just a copy of MapFolder for `HList`s from shapeless. 
It can be done as a combination of MapToList and list fold, but we don't want traverse it twice.
*/
trait MapFoldSet[F <: Poly1, S <: AnyTypeSet, R] extends Fn3[S, R, (R, R) => R] with Constant[R]
  
object MapFoldSet {
  
  def apply[F <: Poly1, S <: AnyTypeSet, R]
    (implicit mapFolder: MapFoldSet[F, S, R]): MapFoldSet[F, S, R] = mapFolder

  implicit def empty[F <: Poly1, R]: MapFoldSet[F, ∅, R] = 
    new MapFoldSet[F, ∅, R] {
      def apply(s: ∅, in: R, op: (R, R) => R): R = in
    }
  
  implicit def cons[F <: Poly1, H, T <: AnyTypeSet, R]
    (implicit 
      hc: Case.Aux[F, H :: HNil, R], 
      tf: MapFoldSet[F, T, R]
    ):  MapFoldSet[F, H :~: T, R] =
    new MapFoldSet[F, H :~: T, R] {
      def apply(s: H :~: T, in: R, op: (R, R) => R): R = op(hc(s.head), tf(s.tail, in, op))
    }
}
