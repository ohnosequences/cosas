package ohnosequences.cosas

object properties {

  // deps
  import types._

  trait AnyProperty extends AnyType

  class Property[V](val label: String) extends AnyProperty { type Raw = V }

  object AnyProperty {

    type ofType[T] = AnyProperty { type Raw = T }

    implicit def propertyOps[P <: AnyProperty](p: P): PropertyOps[P] = new PropertyOps[P](p)
  }

  trait AnyPropertyValueParser {

    type Property <: AnyProperty
    type Value

    val parseValue: Value => Option[Property#Raw]
  }
  case class PropertyValueParser[Vl, P <: AnyProperty](val parseValue: Vl => Option[P#Raw])
  extends AnyPropertyValueParser {

    type Property = P
    type Value = Vl
  }

  trait AnyKeyRep {

    type Property <: AnyProperty
    val property: Property

    val rep: String
  }
  case class KeyRep[P <: AnyProperty](val property: P, val rep: String) {

    type Property = P
  }

  trait AnyPropertyParser { parser =>

    type Property <: AnyProperty
    val property: Property

    type Value
    type From = Map[String, Value]

    type PropertyValueParser <: AnyPropertyValueParser {

      type Property = parser.Property
      type Value = parser.Value
    }
    val propertyValueParser: PropertyValueParser

    // normally property.name
    type KeyRep <: AnyKeyRep { type Property = parser.Property }
    val keyRep: KeyRep

    val parse: From => (Either[AnyPropertyParsingError, ValueOf[Property]], From) =
      map => map get keyRep.rep match {

        case None => ( Left(KeyNotFound(property)), map )

        case Some(v) => propertyValueParser.parseValue(v) match {

          case None => (Left(ErrorParsingValue(property,v)), map)

          case Some(pv) => (Right(property(pv)), map)
        }
      }
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

  class PropertyOps[P <: AnyProperty](val p: P) extends AnyVal {

    def apply(v: P#Raw): ValueOf[P] = valueOf(p)(v)
  }

}
