/*
## Popping an element from a set

It's like `Lookup`, but it removes the element

*/

package ohnosequences.typesets


trait Pop[S <: TypeSet, E] {
  type SOut <: TypeSet
  type EOut <: E
  type Out = (EOut, SOut)
  def apply(s: S): Out
}

object Pop extends Pop_2 {
  type Aux[S <: TypeSet, E, SO <: TypeSet, EO <: E] = Pop[S, E] {
    type SOut = SO
    type EOut = EO
  } 

  type SAux[S <: TypeSet, E, SO <: TypeSet] = Pop[S, E] {
    type SOut = SO
  } 

  type EAux[S <: TypeSet, E, EO <: E] = Pop[S, E] {
    type EOut = EO
  } 


  implicit def foundInHead[E, H <: E , T <: TypeSet]: Pop.Aux[H :~: T, E, T, H] =
    new Pop[H :~: T, E] { 
      type SOut = T
      type EOut = H
      def apply(s: H :~: T): Out = (s.head, s.tail)
    }
}

trait Pop_2 {
  implicit def foundInTail[H, T <: TypeSet, E](implicit e: E ∈ T, l: Pop[T, E]):
    Pop.Aux[H :~: T, E, H :~: l.SOut, l.EOut] =
    new Pop[H :~: T, E] { 
      type SOut = H :~: l.SOut
      type EOut = l.EOut
      def apply(s: H :~: T): Out = {

        // val (e, t) = l(s.tail)
        // (e, s.head :~: t)

        val tpl = l(s.tail)
        (tpl._1, s.head :~: tpl._2)
      }
    }
}
