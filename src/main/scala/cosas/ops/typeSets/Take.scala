/* ## Taking a subset */

package ohnosequences.cosas.ops.typeSets

import ohnosequences.cosas._, fns._, typeSets._

@annotation.implicitNotFound(msg = "Cannot take subset ${Q} from ${S}")
trait Take[S <: AnyTypeSet, Q <: AnyTypeSet] extends Fn1[S] with Out[Q]

object Take {

  def apply[S <: AnyTypeSet, Q <: AnyTypeSet]
    (implicit take: Take[S, Q]): Take[S, Q] = take

  implicit def empty[S <: AnyTypeSet]: 
        Take[S, ∅] = 
    new Take[S, ∅] { def apply(s: S): ∅ = ∅ }

  implicit def cons[S <: AnyTypeSet, S_ <: AnyTypeSet, H, T <: AnyTypeSet]
    (implicit 
      pop: PopSOut[S, H, S_],
      rest: Take[S_, T]
    ):  Take[S, H :~: T] =
    new Take[S, H :~: T] { 

      def apply(s: S): Out = {
        val (h, t) = pop(s)
        h :~: rest(t)
      }
    }
}
