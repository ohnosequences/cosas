package ohnosequences.cosas.ops.records

import ohnosequences.cosas._, fns._, types._, typeSets._, records._, properties._
import ops.typeSets._

@annotation.implicitNotFound(msg = """
  Cannot parse fields:

    ${PS}

  from a Map[String, ${V}].
  Probably you haven't provided property parsers for all properties in

    ${PS}
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

@annotation.implicitNotFound(msg = """
  Cannot serialize fields:

    ${PS}

  to a Map[String, ${V}].
  Probably you haven't provided property serializers for all properties in:

    ${PS}
""")
trait SerializePropertiesTo[PS <: AnyPropertySet, V] extends Fn2[Map[String,V], PS#Raw]
  with Out[ Either[AnyPropertySerializationError, Map[String,V]] ]

  object SerializePropertiesTo {

    def apply[F <: AnyPropertySet, V]
      (implicit serializer: SerializePropertiesTo[F, V]): SerializePropertiesTo[F, V] = serializer

    implicit def empty[V]:
          (□ SerializePropertiesTo V) =
      new (□ SerializePropertiesTo V) {

        def apply(map: Map[String,V], t: ∅): Out = Right(map)
      }

    implicit def cons[
      V,
      H <: AnyProperty, T <: AnyPropertySet,
      PH <: AnyPropertySerializer { type Property = H; type Value = V }
    ](implicit
      serializeP: PH,
      serializeT: SerializePropertiesTo[T, V]
    ):  ( (H :&: T) SerializePropertiesTo V ) =
    new ( (H :&: T) SerializePropertiesTo V ) {

      def apply(map: Map[String,V], pvs: ValueOf[H] :~: T#Raw): Out = {

        val errOrMap = serializeP.serialize(map, pvs.head.value)

        errOrMap match {

          case Left(err) => Left(err)

          case Right(map) => serializeT(map, pvs.tail) match {

            case Left(err) => Left(err)

            case Right(fmap) => Right(fmap)
          }
        }
      }
    }
  }
