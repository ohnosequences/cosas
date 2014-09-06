/*
## Popping an element from a set

It's like `Lookup`, but it removes the element

*/

package ohnosequences.pointless.ops.record

import ohnosequences.pointless._
import AnyFn._, AnyTaggedType.Tagged, AnyProperty._, AnyTypeSet._, AnyRecord._
import ops.typeSet._

@annotation.implicitNotFound(msg = "Can't get property ${P} of the record ${R}")
trait Get[R <: AnyRecord, P <: AnyProperty] 
  extends Fn1[Tagged[R]] with Out[Tagged[P]]

object Get {

  implicit def getter[R <: AnyRecord, P <: AnyProperty]
    (implicit 
      isThere: P ∈ PropertiesOf[R],
      pop: RawOf[R] Pop Tagged[P]
    ):  Get[R, P] = 
    new Get[R, P] { def apply(recEntry: Tagged[R]): Out = pop(recEntry)._1 }

}
