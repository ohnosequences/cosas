package ohnosequences.pointless.impl.ops

import ohnosequences.pointless._, AnyFn._, impl._, typeSet._
import shapeless._, poly._
  
// /* Mapping a set to another set, i.e. the results of mapping should have distinct types */  
// @annotation.implicitNotFound(msg = "Can't map ${F} over ${S} (maybe the resulting types are not distinct)")
// trait SetMapper[F <: Poly1, S <: AnyTypeSet] extends Fn2[F,S] with AnyFn.WithCodomain[AnyTypeSet] {

//   type Out <: AnyTypeSet

//   def apply(s: S): Out
// }

// object SetMapper {

//   def apply[F <: Poly1, S <: AnyTypeSet](implicit mapper: SetMapper[F,S]): Aux[F, S, mapper.Out] = mapper

//   type Aux[F <: Poly1, S <: AnyTypeSet, O <: AnyTypeSet] = SetMapper[F, S] { type Out = O }
  
//   implicit def emptyMapper[F <: Poly1]: Aux[F, ∅, ∅] = new SetMapper[F, ∅] {
      
//       type Out = ∅
//       def apply(s: ∅): Out = ∅
//     }
  
//   implicit def consMapper [
//     F <: Poly1,
//     H, OutH,
//     T <: AnyTypeSet, OutT <: AnyTypeSet
//   ](implicit
//     h: Case1.Aux[F, H, OutH], 
//     t: Aux[F, T, OutT],
//     e: OutH ∉ OutT  // the key check here
//   )
//   : Aux[F, H :~: T, OutH :~: OutT] = new SetMapper[F, H :~: T] { 

//         type Out = OutH :~: OutT
//         def apply(s: H :~: T): Out = h(s.head) :~: t(s.tail)
//       }
// }


/* Mapping a set to an HList: when you want to preserve precise types, but they are not distinct */
@annotation.implicitNotFound(msg = "Can't map ${F} over ${S} to an HList")
trait MapToHList[F <: Poly1, S <: AnyTypeSet] extends Fn1[S] with WithCodomain[HList]

object MapToHList {

  def apply[F <: Poly1, S <: AnyTypeSet]
    (implicit mapper: MapToHList[F, S]): MapToHList[F, S] with out[mapper.Out] = mapper

  // type Aux[S <: AnyTypeSet, F <: Poly1, O <: HList] = MapToHList[S, F] { type Out = O }
  
  implicit def emptyMapToHList[F <: Poly1]: MapToHList[F, ∅] with out[HNil] =
    new MapToHList[F, ∅] {
      type Out = HNil
      def apply(s: ∅): Out = HNil
    }
  
  implicit def consMapToHList[
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

  def apply[F <: Poly1, S <: AnyTypeSet](implicit mapper: MapToList[F, S]): MapToList[F, S] with o[mapper.O] = mapper
  
  implicit def emptyMapToList[F <: Poly1, X]: MapToList[F, ∅] with o[X] = 
    new MapToList[F, ∅] { 
      type O = X
      def apply(s: ∅): Out = Nil
    }
  
  implicit def oneMapToList[H, F <: Poly1, X]
    (implicit h: Case1.Aux[F, H, X]): MapToList[F, H :~: ∅] with o[X] = 
      new MapToList[F, H :~: ∅] {
        type O = X
        def apply(s: H :~: ∅): Out = List[X](h(s.head))
      }

  implicit def consMapToList[H1, H2, T <: AnyTypeSet, F <: Poly1, X]
    (implicit
      h: Case1.Aux[F, H1, X], 
      t: MapToList[F, H2 :~: T] with o[X]
    ):  MapToList[F, H1 :~: H2 :~: T] with o[X] = 
    new MapToList[F, H1 :~: H2 :~: T] { 
      type O = X
      def apply(s: H1 :~: H2 :~: T): Out = h(s.head) :: t(s.tail)
    }
}
