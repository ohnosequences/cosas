/*
## Popping an element from a set

It's like `Lookup`, but it removes the element

*/

package ohnosequences.cosas.ops.records

import ohnosequences.cosas._, fns._, types._, typeSets._, records._
import ops.typeSets._

@annotation.implicitNotFound(msg = "Can't transform ${R} to ${Other} with values ${Rest}")
trait Transform[R <: AnyRecord, Other <: AnyRecord, Rest <: AnyTypeSet] 
  extends Fn3[R#Raw, Other, Rest] with Out[ValueOf[Other]]

object Transform {

  implicit def transform[
      R <: AnyRecord,
      Other <: AnyRecord,
      Rest <: AnyTypeSet, 
      Uni <: AnyTypeSet,
      Missing <: AnyTypeSet
    ](implicit
      missing: (Other#Raw \ R#Raw) { type Out = Missing },
      allMissing: Rest ~:~ Missing,
      uni: (R#Raw ∪ Rest) { type Out = Uni },
      project: Take[Uni, Other#Raw]
    ):  Transform[R, Other, Rest] = 
    new Transform[R, Other, Rest] {

      def apply(recRaw: R#Raw, other: Other, rest: Rest): Out = 
        other := project(uni(recRaw, rest))
    }

}
