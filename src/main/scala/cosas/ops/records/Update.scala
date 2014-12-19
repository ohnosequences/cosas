/*
## Popping an element from a set

It's like `Lookup`, but it removes the element

*/

package ohnosequences.cosas.ops.records

import ohnosequences.cosas._, fns._, types._, typeSets._, records._
import ops.typeSets._

@annotation.implicitNotFound(msg = "Can't update record ${R} with property values ${Ps}")
trait Update[R <: AnyRecord, Ps <: AnyTypeSet]
  extends Fn2[R#Raw, Ps] with Out[ValueOf[R]]

object Update {

  implicit def update[R <: AnyRecord, Ps <: AnyTypeSet]
    (implicit 
      check: Ps ⊂ R#Raw,
      replace: Replace[R#Raw, Ps]
    ):  Update[R, Ps] =
    new Update[R, Ps] {

      def apply(recRaw: R#Raw, propReps: Ps): Out = 

        new ValueOf[R](replace(recRaw, propReps))
    }

}
