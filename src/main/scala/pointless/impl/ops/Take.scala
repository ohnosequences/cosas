/* ## Taking a subset */

package ohnosequences.pointless.impl.ops

import ohnosequences.pointless._, AnyFn._, impl._, typeSet._

@annotation.implicitNotFound(msg = "Cannot take subset ${Q} from ${S}")
trait Take[S <: AnyTypeSet, Q <: AnyTypeSet] extends Fn1[S] with Constant[Q]

object Take {
  implicit def empty[S <: AnyTypeSet]: 
        Take[S, ∅] = 
    new Take[S, ∅] { def apply(s: S): ∅ = ∅ }

  implicit def cons[S <: AnyTypeSet, S_ <: AnyTypeSet, H, T <: AnyTypeSet]
    (implicit 
      pop: Pop.Aux[S, H, S_, H],
      rest: Take[S_, T]
    ):  Take[S, H :~: T] =
    new Take[S, H :~: T] { 
      def apply(s: S) = {
        val tpl = pop(s)
        tpl._1 :~: rest(tpl._2)
      }
    }
}
