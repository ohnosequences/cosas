package ohnosequences.pointless.ops.typeSet

import ohnosequences.pointless._, AnyFn._, AnyTypeSet._
import shapeless._, poly._
  

/* Mapping a set to an HList: when you want to preserve precise types, but they are not distinct */
@annotation.implicitNotFound(msg = "Can't map ${F} over ${S} to an HList")
trait MapToHList[F <: Poly1, S <: AnyTypeSet] extends Fn1[S] with OutBound[HList]

object MapToHList {

  def apply[F <: Poly1, S <: AnyTypeSet]
    (implicit mapper: MapToHList[F, S]): MapToHList[F, S] = mapper

  implicit def empty[F <: Poly1, E <: AnyEmptySet]:
        MapToHList[F, E] with Out[HNil] =
    new MapToHList[F, E] with Out[HNil] { def apply(s: In1): Out = HNil }
  
  implicit def cons[
    F <: Poly1, 
    H <: T#Bound, T <: AnyTypeSet, 
    OutH, OutT <: HList
  ](implicit
    h: Case1.Aux[F, H, OutH], 
    t: MapToHList[F, T] { type Out = OutT }
  ):  MapToHList[F, H :~: T] with Out[OutH :: OutT] = 
  new MapToHList[F, H :~: T] with Out[OutH :: OutT] { 

    def apply(s: In1): Out = h(s.head) :: t(s.tail:T)
  }
}

/* Mapping a set to a List: normally, when you are mapping everything to one type */
@annotation.implicitNotFound(msg = "Can't map ${F} over ${S} to a List")
trait MapToList[F <: Poly1, S <: AnyTypeSet] extends Fn1[S] with OutInContainer[List] 

object MapToList {

  def apply[F <: Poly1, S <: AnyTypeSet]
    (implicit mapper: MapToList[F, S]): MapToList[F, S] = mapper
  
  implicit def empty[F <: Poly1, E <: AnyEmptySet, X]:
        MapToList[F, E] with InContainer[X] = 
    new MapToList[F, E] with InContainer[X] { def apply(s: In1): Out = Nil }
  
  implicit def one[B, H <: B, F <: Poly1, X]
    (implicit h: Case1.Aux[F, H, X]): 
          MapToList[F, H :~: EmptySet.Of[B]] with InContainer[X] = 
      new MapToList[F, H :~: EmptySet.Of[B]] with InContainer[X] { 

        def apply(s: In1): Out = List[X](h(s.head))
      }

  implicit def cons2[H1 <: T#Bound, H2 <: T#Bound, T <: AnyTypeSet, F <: Poly1, X]
    (implicit
      h: Case1.Aux[F, H1, X], 
      t: MapToList[F, H2 :~: T] { type O = X }
    ):  MapToList[F, H1 :~: H2 :~: T] with InContainer[X] = 
    new MapToList[F, H1 :~: H2 :~: T] with InContainer[X] {

      def apply(s: In1): Out = h(s.head) :: t(s.tail)
    }
}

/* Mapping a set to another set, i.e. the results of mapping should have distinct types */  
@annotation.implicitNotFound(msg = "Can't map ${F} over ${S} (maybe the resulting types are not distinct)")
trait MapSet[F <: Poly1, S <: AnyTypeSet] extends Fn1[S] with OutBound[AnyTypeSet]

object MapSet {

  def apply[F <: Poly1, S <: AnyTypeSet]
    (implicit mapper: MapSet[F, S]): MapSet[F, S] = mapper

  implicit def empty[F <: Poly1, E <: AnyEmptySet, FB]
    (implicit b: Case1[F, E#Bound] { type Result = FB }):
        MapSet[F, E] with Out[∅[FB]] = 
    new MapSet[F, E] with Out[∅[FB]] { def apply(s: In1): Out = ∅[FB] }
  
  implicit def cons[FB,
    F <: Poly1,
    H <: T#Bound, OutH <: FB,
    T <: AnyTypeSet, OutT <: TypeSet.Of[FB]
  ](implicit
    f: Case1[F, T#Bound] { type Result = FB },
    h: Case1[F, H] { type Result = OutH },
    t: MapSet[F, T] { type Out = OutT }
    // e: OutH ∉ OutT  // the key check here
  ):  MapSet[F, H :~: T] with Out[OutH :~: OutT] = 
  new MapSet[F, H :~: T] with Out[OutH :~: OutT] {

    def apply(s: In1): Out = h(s.head) :~: t(s.tail)
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

  implicit def empty[F <: Poly1, E <: AnyEmptySet, R]:
        MapFoldSet[F, E, R] = 
    new MapFoldSet[F, E, R] {

      def apply(s: In1, in: R, op: (R, R) => R): R = in
    }
  
  implicit def cons[F <: Poly1, H <: T#Bound, T <: AnyTypeSet, R]
    (implicit 
      hc: Case.Aux[F, H :: HNil, R], 
      tf: MapFoldSet[F, T, R]
    ):  MapFoldSet[F, H :~: T, R] =
    new MapFoldSet[F, H :~: T, R] {

      def apply(s: In1, in: R, op: (R, R) => R): R = op(hc(s.head), tf(s.tail, in, op))
    }
}
