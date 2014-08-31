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
  extends Fn3[Tagged[R], Other, Rest] { type Out = Tagged[Other] }

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
    ):  Transform[R, Other, Rest] with out[Tagged[Other]] = 
    new Transform[R, Other, Rest] {
      def apply(recEntry: Tagged[R], other: Other, rest: Rest): Out = other =>> project(uni(recEntry, rest))
    }

}


@annotation.implicitNotFound(msg = "Can't parse record ${R} from ${X}")
trait ParseRecordFrom[R <: AnyRecord, X]
  extends Fn2[R, X] { type Out = Tagged[R] }

object ParseRecordFrom {

  def apply[R <: AnyRecord, X]
    (implicit parser: ParseRecordFrom[R, X]): ParseRecordFrom[R, X] with out[parser.Out] = parser

  implicit def any[R <: AnyRecord, X](implicit
    parseSet: (R#Properties ParseFrom X) with out[R#Raw]
  ):  (R ParseRecordFrom X) =
  new (R ParseRecordFrom X) {
    
    def apply(r: R, x: X): Out = r =>> parseSet(r.properties, x)
  }
}
