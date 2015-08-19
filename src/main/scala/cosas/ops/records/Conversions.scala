package ohnosequences.cosas.ops.records

import ohnosequences.cosas._, fns._, types._, typeSets._, records._, properties._
import ops.typeSets._

@annotation.implicitNotFound(msg = "oh no pigeons!")
// NOTE: it should be restricted to AnyTypeSet.Of[AnyType], when :~: is known to return the same thing
trait ParseRecordFrom[Ps <: AnyTypeSet, X] extends Fn2[Ps, Map[String,X]] {

  type Vs <: AnyTypeSet
  type Out = Either[AnyPropertyParsingError, Vs]
}

object ParseRecordFrom {

  def apply[Ps <: AnyTypeSet, V]
    (implicit parser: ParseRecordFrom[Ps, V]): ParseRecordFrom[Ps, V] = parser

  implicit def empty[V]:
        (∅ ParseRecordFrom V) { type Vs = ∅ }  =
    new (∅ ParseRecordFrom V) {

      type Vs = ∅
      def apply(s: ∅, map: Map[String,V]): Out = Right(∅)
    }

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
