/*
## Map-folder for sets 
    
Just a copy of MapFolder for `HList`s from shapeless.
*/
package ohnosequences.typesets

import shapeless.Poly
import shapeless.poly.Case

trait SetMapFolder[S <: TypeSet, F <: Poly, R] extends Fn3[S,F,R] {

  type Out = R

  def apply(s: S, in: R, op: (R, R) => R): Out
}
  
object SetMapFolder {
  import shapeless._
  import poly._
  
  implicit def empty[R, F <: Poly]: SetMapFolder[∅, F, R] = new SetMapFolder[∅, F, R] {

    def apply(s: ∅, in: R, op: (R, R) => R): R = in
  }
  
  implicit def cons[H, T <: TypeSet, F <: Poly, R]
    (implicit hc: Case.Aux[F, H :: HNil, R], tf: SetMapFolder[T, F, R]): SetMapFolder[H :~: T, F, R] =
      new SetMapFolder[H :~: T, F, R] {
        
          def apply(s: H :~: T, in: R, op: (R, R) => R): R = op(hc(s.head), tf(s.tail, in, op))
      }
}
