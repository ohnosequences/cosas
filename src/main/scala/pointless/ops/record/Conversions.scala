/*
## Popping an element from a set

It's like `Lookup`, but it removes the element

*/

package ohnosequences.pointless.ops.record

import ohnosequences.pointless._
import AnyFn._, AnyTaggedType.Tagged, AnyProperty._, AnyTypeSet._, AnyRecord._
import ops.typeSet._

@annotation.implicitNotFound(msg = "Can't transform ${R} to ${Other} with values ${Rest}")
trait Transform[R <: AnyRecord, Other <: AnyRecord, Rest <: AnyTypeSet] 
  extends Fn3[Tagged[R], Other, Rest] with Out[Tagged[Other]]

object Transform {

  implicit def transform[
      R <: AnyRecord,
      Other <: AnyRecord,
      Rest <: AnyTypeSet, 
      Uni <: AnyTypeSet,
      Missing <: AnyTypeSet
    ](implicit
      missing: (RawOf[Other] \ RawOf[R]) with out[Missing],
      allMissing: Rest ~:~ Missing,
      uni: (RawOf[R] ∪ Rest) with out[Uni],
      project: Take[Uni, RawOf[Other]]
    ):  Transform[R, Other, Rest] = 
    new Transform[R, Other, Rest] {

      def apply(recEntry: Tagged[R], other: Other, rest: Rest): Out = other =>> project(uni(recEntry, rest))
    }

}
