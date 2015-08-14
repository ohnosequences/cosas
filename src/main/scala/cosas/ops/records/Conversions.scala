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

import properties._

@annotation.implicitNotFound(msg = "oh no pigeons!")
// NOTE: it should be restricted to AnyTypeSet.Of[AnyType], when :~: is known to return the same thing
trait ParseRecordFrom[Ps <: AnyTypeSet, X] extends Fn2[Ps, Map[String,X]] {

  type Vs <: AnyTypeSet
  type Out = Either[AnyPropertyParsingError, Vs]
}

object ParseFrom {

  // def apply[S <: AnyTypeSet, X]
  //   (implicit parser: ParseFrom[S, X]): ParseFrom[S, X] = parser
  //
  // implicit def empty[X]:
  //       (∅ ParseFrom X) with Out[∅] =
  //   new (∅ ParseFrom X) with Out[∅] {
  //
  //     def apply(s: ∅, x: X): Out = ∅
  //   }

  implicit def cons[
    V,
    P <: AnyProperty, PsT <: AnyTypeSet, VsT <: AnyTypeSet,
    PP <: AnyPropertyParser { type Property = P; type Value = V }
  ](implicit
    parseP: PP,
    parseT: ParseRecordFrom[PsT, V] { type Vs = VsT }
  ):  ( (P :~: PsT) ParseRecordFrom V ) { type Vs = ValueOf[P] :~: VsT } =
  new ( (P :~: PsT) ParseRecordFrom V ) {

    type Vs = ValueOf[P] :~: VsT

    def apply(pvs: P :~: PsT, map: Map[String,V]): Out = {

      val (errOrP, restMap) = parseP.parse(map)

      errOrP match {

        case Left(err) => Left(err)

        case Right(pv) => parseT(pvs.tail, restMap) match {

          case Left(err) => Left(err)

          case Right(pvt) => Right(pv :~: pvt)
        }
      }
    }
  }
}
