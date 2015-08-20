package ohnosequences.cosas.ops.records

import ohnosequences.cosas._, fns._, types._, typeSets._, records._, properties._
import ops.typeSets._

@annotation.implicitNotFound(msg = """
  Cannot parse fields
    ${PS}
  from a Map[String, ${V}].
  Probably you haven't provided property parsers for all properties involved.
""")
trait ParsePropertiesFrom[PS <: AnyPropertySet, V] extends Fn1[Map[String,V]]
  with Out[ Either[AnyPropertyParsingError, PS#Raw] ]

object ParsePropertiesFrom {

  def apply[F <: AnyPropertySet, V]
    (implicit parser: ParsePropertiesFrom[F, V]): ParsePropertiesFrom[F, V] = parser

  implicit def empty[V]:
        (□ ParsePropertiesFrom V)  =
    new (□ ParsePropertiesFrom V) {

      def apply(map: Map[String,V]): Out = Right(∅)
    }

  implicit def cons[
    V,
    H <: AnyProperty, T <: AnyPropertySet,
    PH <: AnyPropertyParser { type Property = H; type Value = V }
  ](implicit
    parseP: PH,
    parseT: ParsePropertiesFrom[T, V]
  ):  ( (H :&: T) ParsePropertiesFrom V ) =
  new ( (H :&: T) ParsePropertiesFrom V ) {

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
