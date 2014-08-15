/* 
## Reordering set

Just a combination of ~:~ and Take (reordering set)
*/

package ohnosequences.pointless.impl.ops

import ohnosequences.pointless._, AnyFn._, impl._, typeSet._

@annotation.implicitNotFound(msg = "Can't reorder ${S} to ${Q}")
trait As[S <: AnyTypeSet, Q <: AnyTypeSet] extends Fn1[S] with Constant[Q] 

object As {
  // TODO why not one in the other direction??
  implicit def any[S <: AnyTypeSet, Out <: AnyTypeSet]
    (implicit eq: S ~:~ Out, project: Take[S, Out]): 
        As[S, Out] = 
    new As[S, Out] { def apply(s: S): Out = project(s) }
}
