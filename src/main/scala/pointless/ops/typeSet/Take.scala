/* ## Taking a subset */

package ohnosequences.pointless.ops.typeSet

import ohnosequences.pointless._, AnyFn._, AnyTypeSet._

@annotation.implicitNotFound(msg = "Cannot take subset ${Q} from ${S}")
trait Take[S <: AnyTypeSet, Q <: AnyTypeSet] extends Fn1[S] with Out[Q]

object Take {

  def apply[S <: AnyTypeSet, Q <: AnyTypeSet]
    (implicit take: Take[S, Q]): Take[S, Q] = take

  implicit def empty[S <: AnyTypeSet, B]: 
        Take[S, ∅[B]] = 
    new Take[S, ∅[B]] { def apply(s: S): Out = ∅[B] }

  implicit def cons[S <: AnyTypeSet, S_ <: AnyTypeSet, H <: T#Bound, T <: AnyTypeSet]
    (implicit 
      pop: Pop[S, H] { type SOut = S_ },
      rest: Take[S_, T]
    ):  Take[S, H :~: T] =
    new Take[S, H :~: T] { 

      def apply(s: S): Out = {
        val (h, t) = pop(s)
        h :~: rest(t)
      }
    }
}
