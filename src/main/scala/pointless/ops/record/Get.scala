/*
## Popping an element from a set

It's like `Lookup`, but it removes the element

*/

package ohnosequences.pointless.ops.record

import ohnosequences.pointless._, AnyFn._, AnyTaggedType._, AnyProperty._, AnyTypeSet._, AnyRecord._

@annotation.implicitNotFound(msg = "Can't get property ${P} of the record ${R}")
trait Get[R <: AnyRecord, P <: AnyProperty] extends Fn1[Tagged[R]] with Constant[Tagged[P]] 

object Get {

  implicit def getter[R <: AnyRecord, P <: AnyProperty]
    (implicit 
      isThere: P ∈ PropertiesOf[R],
      lookup: Lookup[RawOf[R], Tagged[P]]
    ):  Get[R, P] with out[Tagged[P]] = 
    new Get[R, P] {
      def apply(recEntry: Tagged[R]): Out = lookup(recEntry)
    }

}
