/*
## Popping an element from a set

It's like `Lookup`, but it removes the element

*/

package ohnosequences.pointless.ops.record

import ohnosequences.pointless._, AnyFn._, representable._, property._, typeSet._, record._

@annotation.implicitNotFound(msg = "Can't get property ${P} of the record ${R}")
trait Get[R <: AnyRecord, P <: AnyProperty] extends Fn1[RepOf[R]] with Constant[RepOf[P]] 

object Get {

  implicit def getter[R <: AnyRecord, P <: AnyProperty]
    (implicit 
      isThere: P ∈ PropertiesOf[R],
      lookup: Lookup[RawOf[R], RepOf[P]]
    ):  Get[R, P] with out[RepOf[P]] = 
    new Get[R, P] {
      def apply(recEntry: RepOf[R]): Out = lookup(recEntry)
    }

}
