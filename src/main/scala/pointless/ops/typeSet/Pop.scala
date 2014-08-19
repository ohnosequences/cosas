/*
## Popping an element from a set

It's like `Lookup`, but it removes the element

*/

package ohnosequences.pointless.ops.typeSet

import ohnosequences.pointless._, AnyFn._, typeSet._

@annotation.implicitNotFound(msg = "Can't pop an element of type ${E} from the set ${S}")
trait Pop[S <: AnyTypeSet, E] extends Fn1[S] with WithCodomain[(E, AnyTypeSet)] {
  type SOut <: AnyTypeSet
  type EOut <: E
  type Out = (EOut, SOut)
}

object Pop extends Pop_2 {
  def apply[S <: AnyTypeSet, E](implicit pop: Pop[S, E]): Pop[S, E] with out[pop.Out] = pop

  type Aux[S <: AnyTypeSet, E, SO <: AnyTypeSet, EO <: E] = Pop[S, E] {
    type SOut = SO
    type EOut = EO
  } 

  implicit def foundInHead[E, H <: E , T <: AnyTypeSet]: Pop.Aux[H :~: T, E, T, H] =
    new Pop[H :~: T, E] { 
      type SOut = T
      type EOut = H
      def apply(s: H :~: T): Out = (s.head, s.tail)
    }
}

trait Pop_2 {
  implicit def foundInTail[H, T <: AnyTypeSet, E](implicit e: E ∈ T, l: Pop[T, E]):
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
