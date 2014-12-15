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
  implicit def any[S <: AnyTypeSet, O <: AnyTypeSet]
    (implicit 
      eq: S ~:~ O, 
      take: Take[S, O]
    ):  (S ReorderTo O) =
    new (S ReorderTo O) { def apply(s: S): O = take(s) }
}
