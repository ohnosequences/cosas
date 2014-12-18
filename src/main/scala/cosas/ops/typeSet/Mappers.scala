package ohnosequences.cosas.ops.typeSet

import ohnosequences.cosas._, fn._, typeSet._
import shapeless._, poly._
  

/* Mapping a set to an HList: when you want to preserve precise types, but they are not distinct */
@annotation.implicitNotFound(msg = "Can't map ${F} over ${S} to an HList")
trait MapToHList[F <: Poly1, S <: AnyTypeSet] extends Fn1[S] with OutBound[HList]

object MapToHList {

  def apply[F <: Poly1, S <: AnyTypeSet]
    (implicit mapper: MapToHList[F, S]): MapToHList[F, S] = mapper

  implicit def empty[F <: Poly1]: 
        MapToHList[F, ∅] with Out[HNil] =
    new MapToHList[F, ∅] with Out[HNil] { def apply(s: ∅): Out = HNil }
  
  implicit def cons[
    F <: Poly1, 
    H, T <: AnyTypeSet, 
    OutH, OutT <: HList
  ](implicit
    h: Case1.Aux[F, H, OutH], 
    t: MapToHList[F, T] { type Out = OutT }
  ):  MapToHList[F, H :~: T] with Out[OutH :: OutT] = 
  new MapToHList[F, H :~: T] with Out[OutH :: OutT] { 

    def apply(s: H :~: T): Out = h(s.head) :: t(s.tail:T)
  }
}

/* Mapping a set to a List: normally, when you are mapping everything to one type */
@annotation.implicitNotFound(msg = "Can't map ${F} over ${S} to a List")
trait MapToList[F <: Poly1, S <: AnyTypeSet] extends Fn1[S] with OutInContainer[List] 

object MapToList {

  def apply[F <: Poly1, S <: AnyTypeSet]
    (implicit mapper: MapToList[F, S]): MapToList[F, S] = mapper
  
  implicit def empty[F <: Poly1, X]: 
        MapToList[F, ∅] with InContainer[X] = 
    new MapToList[F, ∅] with InContainer[X] { def apply(s: ∅): Out = Nil }
  
  implicit def one[H, F <: Poly1, X]
    (implicit h: Case1.Aux[F, H, X]): 
          MapToList[F, H :~: ∅] with InContainer[X] = 
      new MapToList[F, H :~: ∅] with InContainer[X] { 

        def apply(s: H :~: ∅): Out = List[X](h(s.head))
      }

  implicit def cons2[H1, H2, T <: AnyTypeSet, F <: Poly1, X]
    (implicit
      h: Case1.Aux[F, H1, X], 
      t: MapToList[F, H2 :~: T] { type O = X }
    ):  MapToList[F, H1 :~: H2 :~: T] with InContainer[X] = 
    new MapToList[F, H1 :~: H2 :~: T] with InContainer[X] {

      def apply(s: H1 :~: H2 :~: T): Out = h(s.head) :: t(s.tail)
    }
}

/* Mapping a set to another set, i.e. the results of mapping should have distinct types */  
@annotation.implicitNotFound(msg = "Can't map ${F} over ${S} (maybe the resulting types are not distinct)")
trait MapSet[F <: Poly1, S <: AnyTypeSet] extends Fn1[S] with OutBound[AnyTypeSet]

object MapSet {

  def apply[F <: Poly1, S <: AnyTypeSet]
    (implicit mapper: MapSet[F, S]): MapSet[F, S] = mapper

  implicit def empty[F <: Poly1]:
        MapSet[F, ∅] with Out[∅] = 
    new MapSet[F, ∅] with Out[∅] { def apply(s: ∅): Out = ∅ }
  
  implicit def cons[
    F <: Poly1,
    H, OutH,
    T <: AnyTypeSet, OutT <: AnyTypeSet
  ](implicit
    h: Case1.Aux[F, H, OutH], 
    t: MapSet[F, T] { type Out = OutT },
    e: OutH ∉ OutT  // the key check here
  ):  MapSet[F, H :~: T] with Out[OutH :~: OutT] = 
  new MapSet[F, H :~: T] with Out[OutH :~: OutT] {

    def apply(s: H :~: T): Out = h(s.head) :~: t(s.tail)
  }
}

/*
Map-folder for sets 
    
Just a copy of MapFolder for `HList`s from shapeless. 
It can be done as a combination of MapToList and list fold, but we don't want traverse it twice.
*/
trait MapFoldSet[F <: Poly1, S <: AnyTypeSet, R] 
  extends Fn3[S, R, (R, R) => R] with Out[R]
  
object MapFoldSet {
  
  def apply[F <: Poly1, S <: AnyTypeSet, R]
    (implicit mapFolder: MapFoldSet[F, S, R]): MapFoldSet[F, S, R] = mapFolder

  implicit def empty[F <: Poly1, R]: 
        MapFoldSet[F, ∅, R] = 
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
