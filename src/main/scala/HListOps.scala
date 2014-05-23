/*
## Map-folder for sets 
    
Just a copy of MapFolder for `TypeSet`s from shapeless.
*/

package ohnosequences.typesets

import shapeless._

trait ToHList[S <: TypeSet] extends DepFn1[S] { type Out <: HList }

object ToHList {
  def apply[S <: TypeSet](s: S)(implicit toHList: ToHList[S]): Aux[S, toHList.Out] = toHList

  type Aux[S <: TypeSet, L <: HList] = ToHList[S] { type Out = L }

  implicit def emptyToHList: Aux[∅, HNil] = 
    new ToHList[∅] { type Out = HNil
      def apply(s: ∅): Out = HNil
    }
  
  implicit def consToHList[H, T <: TypeSet, LT <: HList]
      (implicit lt : Aux[T, LT]): Aux[H :~: T, H :: LT] = 
    new ToHList[H :~: T] { type Out = H :: LT
      def apply(s: H :~: T): Out = s.head :: lt(s.tail)
    }
}

// TODO: FromHList
