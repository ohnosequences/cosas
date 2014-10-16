/* 
## Reordering set

Just a combination of ~:~ and Take (reordering set)
*/

package ohnosequences.cosas.ops.typeSet

import ohnosequences.cosas._, AnyFn._, AnyTypeSet._

@annotation.implicitNotFound(msg = "Can't reorder ${S} to ${Q}")
trait ReorderTo[S <: AnyTypeSet, Q <: AnyTypeSet]
  extends Fn1[S] with Out[Q]

object ReorderTo {
  def apply[S <: AnyTypeSet, Q <: AnyTypeSet]
    (implicit reorder: S ReorderTo Q): S ReorderTo Q = reorder

  // TODO why not one in the other direction??
  implicit def any[S <: AnyTypeSet, Out <: AnyTypeSet]
    (implicit 
      eq: S ~:~ Out, 
      take: Take[S, Out]
    ):  (S ReorderTo Out) =
    new (S ReorderTo Out) { def apply(s: S): Out = take(s) }
}
