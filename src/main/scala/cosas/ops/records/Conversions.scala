package ohnosequences.cosas.ops.records

import ohnosequences.cosas._, fns._, types._, typeSets._, records._, properties._
import ops.typeSets._

@annotation.implicitNotFound(msg = "oh no pigeons!")
// NOTE: it should be restricted to AnyTypeSet.Of[AnyType], when :~: is known to return the same thing
trait ParseFieldsFrom[Fs <: AnyFields, V] extends Fn1[Map[String,V]] {

  type Out = Either[AnyPropertyParsingError, Fs#Values]
}

object ParseFieldsFrom {

  def apply[F <: AnyFields, V]
    (implicit parser: ParseFieldsFrom[F, V]): ParseFieldsFrom[F, V] = parser

  implicit def empty[V]:
        (□ ParseFieldsFrom V)  =
    new (□ ParseFieldsFrom V) {

      def apply(map: Map[String,V]): Out = Right(∅)
    }

  implicit def cons[
    V,
    H <: AnyProperty, T <: AnyFields,
    PH <: AnyPropertyParser { type Property = H; type Value = V }
  ](implicit
    parseP: PH,
    parseT: ParseFieldsFrom[T, V]
  ):  ( (H :&: T) ParseFieldsFrom V ) =
  new ( (H :&: T) ParseFieldsFrom V ) {

    def apply(map: Map[String,V]): Out = {

      val (errOrP, restMap) = parseP.parse(map)

      errOrP match {

        case Left(err) => Left(err)

        case Right(pv) => parseT(restMap) match {

          case Left(err) => Left(err)

          case Right(pvt) => Right(pv :~: pvt)
        }
      }
    }
  }
}
