/*
## Popping an element from a set

It's like `Lookup`, but it removes the element

*/

package ohnosequences.pointless.ops.record

import ohnosequences.pointless._, AnyFn._, AnyTaggedType._, AnyProperty._, AnyTypeSet._, AnyRecord._

@annotation.implicitNotFound(msg = "Can't update record ${R} with property values ${Ps}")
trait Update[R <: AnyRecord, Ps <: AnyTypeSet]
  extends Fn2[Tagged[R], Ps] { type Out = Tagged[R] }

object Update {

  implicit def update[R <: AnyRecord, Ps <: AnyTypeSet]
    (implicit 
      check: Ps ⊂ Tagged[R],
      replace: Replace[Tagged[R], Ps]
    ):  Update[R, Ps] with out[Tagged[R]] = 
    new Update[R, Ps] {
      def apply(recEntry: Tagged[R], propReps: Ps): Out = replace(recEntry, propReps)
    }

}
