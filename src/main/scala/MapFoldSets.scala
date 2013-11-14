/*
## Map-folder for sets 
    
Just a copy of MapFolder for `HList`s from shapeless.
*/

package ohnosequences.typesets

import shapeless.poly.Case

trait SetMapFolder[S <: TypeSet, R, F] {
  def apply(s: S, in: R, op: (R, R) => R): R 
}
  
object SetMapFolder {
  import shapeless._
  import shapeless.Poly._
  
  implicit def empty[R, F] = new SetMapFolder[∅, R, F] {
    def apply(s: ∅, in: R, op: (R, R) => R) = in
  }
  
  implicit def cons[H, T <: TypeSet, R, F <: Poly]
    (implicit hc: Case.Aux[F, H, R], tf: SetMapFolder[T, R, F]) =
      new SetMapFolder[H :+: T, R, F] {
          def apply(s: H :+: T, in: R, op: (R, R) => R) = op(hc(s.head), tf(s.tail, in, op))
      }
}
