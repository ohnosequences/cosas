package ohnosequences.cosas

object properties {

  // deps
  import types._

  trait AnyProperty extends AnyType

  class Property[V](val label: String) extends AnyProperty { type Raw = V }

  case object AnyProperty {

    type ofType[T] = AnyProperty { type Raw = T }

    implicit def propertyOps[P <: AnyProperty](p: P): PropertyOps[P] = PropertyOps(p)
  }

  trait AnyPropertyParser { parser =>

    type Property <: AnyProperty
    val property: Property

    type Value
    type From = Map[String, Value]

    val propertyValueParser: Value => Option[Property#Raw]

    val keyRep: String

    val parse: From => (Either[AnyPropertyParsingError, ValueOf[Property]], From) =
      map => map get keyRep match {

        case None => ( Left(KeyNotFound(property)), map )

        case Some(v) => propertyValueParser(v) match {

          case None => (Left(ErrorParsingValue(property,v)), map)

          case Some(pv) => (Right(property(pv)), map)
        }
      }
  }
  case class PropertyParser[P <: AnyProperty,V](
    val property: P,
    val keyRep: String,
    val propertyValueParser: V => Option[P#Raw]
  ) extends AnyPropertyParser {

    type Property = P
    type Value = V
  }

  sealed trait AnyPropertyParsingError {

    type Property <: AnyProperty
    type Value
    type From = Map[String, Value]
  }
  case class KeyNotFound[P <: AnyProperty, Vl](val p: P)
  extends AnyPropertyParsingError {

    type Property = P
    type Value = Vl
  }

  case class ErrorParsingValue[P <: AnyProperty, Vl](val p: P, val value: Vl)
  extends AnyPropertyParsingError {

    type Property = P
    type Value = Vl
  }

  case class PropertyOps[P <: AnyProperty](val p: P) extends AnyVal {

    def apply(v: P#Raw): ValueOf[P] = valueOf(p)(v)
  }

}
