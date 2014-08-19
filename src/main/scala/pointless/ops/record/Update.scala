/*
## Popping an element from a set

It's like `Lookup`, but it removes the element

*/

package ohnosequences.pointless.ops.record

import ohnosequences.pointless._, AnyFn._, representable._, property._, typeSet._, record._

@annotation.implicitNotFound(msg = "Can't update record ${R} with property values ${Ps}")
trait Update[R <: AnyRecord, Ps <: AnyTypeSet] extends Fn2[RepOf[R], Ps] with Constant[RepOf[R]]

object Update {

  implicit def update[R <: AnyRecord, Ps <: AnyTypeSet]
    (implicit 
      check: Ps ⊂ RepOf[R],
      replace: Replace[RepOf[R], Ps]
    ):  Update[R, Ps] with out[RepOf[R]] = 
    new Update[R, Ps] {
      def apply(recEntry: RepOf[R], propReps: Ps): Out = replace(recEntry, propReps)
    }

}
