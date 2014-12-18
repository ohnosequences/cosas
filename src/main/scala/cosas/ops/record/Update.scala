/*
## Popping an element from a set

It's like `Lookup`, but it removes the element

*/

package ohnosequences.cosas.ops.record

import ohnosequences.cosas._, fn._, denotation._, AnyProperty._, AnyTypeSet._, AnyRecord._
import ops.typeSet._

@annotation.implicitNotFound(msg = "Can't update record ${R} with property values ${Ps}")
trait Update[R <: AnyRecord, Ps <: AnyTypeSet]
  extends Fn2[RawOf[R], Ps] with Out[ValueOf[R]]

object Update {

  implicit def update[R <: AnyRecord, Ps <: AnyTypeSet]
    (implicit 
      check: Ps ⊂ RawOf[R],
      replace: Replace[RawOf[R], Ps]
    ):  Update[R, Ps] =
    new Update[R, Ps] {

      def apply(recRaw: RawOf[R], propReps: Ps): Out = 

        new ValueOf[R](replace(recRaw, propReps))
    }

}
