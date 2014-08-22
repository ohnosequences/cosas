/* ## Taking a subset */

package ohnosequences.pointless.ops.typeSet

import ohnosequences.pointless._, AnyFn._, AnyTypeSet._

@annotation.implicitNotFound(msg = "Cannot take subset ${Q} from ${S}")
trait Take[S <: AnyTypeSet, Q <: AnyTypeSet]
  extends Fn1[S] { type Out = Q }

object Take {

  def apply[S <: AnyTypeSet, Q <: AnyTypeSet]
    (implicit take: Take[S, Q]): Take[S, Q] with out[take.Out] = take

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
