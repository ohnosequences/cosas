package ohnosequences.cosas

object properties {

  // deps
  import types._, typeSets._

  trait AnyProperty extends AnyType

  class Property[V](val label: String) extends AnyProperty { type Raw = V }

  case object AnyProperty {

    type ofType[T] = AnyProperty { type Raw = T }

    implicit def propertyOps[P <: AnyProperty](p: P): PropertyOps[P] = PropertyOps(p)
  }

  trait AnyPropertySerializer { serializer =>

    type Property <: AnyProperty
    val property: Property

    type Value
    type To = Map[String, Value]

    val propertyValueSerializer: Property#Raw => Option[Value]

    val keyRep: String

    val serialize: (To, Property#Raw) => Either[AnyPropertySerializationError, To] =
      (map, value) => map get keyRep match {

        case Some(v) => Left(KeyPresent(property, keyRep))

        case None => propertyValueSerializer(value) match {

          case None => Left(ErrorSerializingValue(property, property(value)))

          case Some(pv) => Right(map + (keyRep -> pv) )
        }
      }
  }

  case class PropertySerializer[P <: AnyProperty,V](
    val property: P,
    val keyRep: String)(
    val propertyValueSerializer: P#Raw => Option[V]
  ) extends AnyPropertySerializer {

    type Property = P
    type Value = V
  }



  sealed trait AnyPropertySerializationError {

    type Property <: AnyProperty
    type Value
    type To = Map[String, Value]
  }
  case class KeyPresent[P <: AnyProperty, Vl](val p: P, val keyRep: String)
  extends AnyPropertySerializationError {

    type Property = P
    type Value = Vl
  }

  case class ErrorSerializingValue[P <: AnyProperty, Vl](val p: P, val value: ValueOf[P])
  extends AnyPropertySerializationError {

    type Property = P
    type Value = Vl
  }

  trait AnyPropertyParser extends AnyTypeParser { parser =>

    type Type <: AnyProperty
    type D = Type#Raw
  }
  case class PropertyParser[P <: AnyProperty,V0](
    val tpe: P,
    val labelRep: String)(
    val denotationParser: V0 => Option[P#Raw]
  ) extends AnyPropertyParser {

    type Type = P
    type V = V0
  }

  case class PropertyOps[P <: AnyProperty](val p: P) extends AnyVal {

    def apply(v: P#Raw): ValueOf[P] = valueOf(p)(v)
  }

  /*
    ## PropertySet

    PropertySet wraps a typeset of properties, constructing along the way the typeset of their `ValueOf`s.
  */
  trait AnyPropertySet extends AnyType {

    type Properties <: AnyTypeSet // of AnyProperty's
    val  properties: Properties

    type Raw <: AnyTypeSet

    type Size = Properties#Size
  }

  case object EmptyPropertySet extends AnyPropertySet {

    type Properties = ∅
    val  properties = ∅

    type Raw = ∅

    val label: String = "□"
  }
  type EmptyPropertySet = EmptyPropertySet.type

  /* An alias for unicode-lovers */
  type  □   = EmptyPropertySet
  val □ : □ = EmptyPropertySet

  // TODO review this symbol; I'm fine with any other
  case class :&:[P <: AnyProperty, T <: AnyPropertySet]
  (val head: P, val tail: T)(implicit val headIsNew: P ∉ T#Properties) extends AnyPropertySet {

    type Properties = P :~: T#Properties
    val  properties: Properties = head :~: (tail.properties: T#Properties)

    type Raw = ValueOf[P] :~: T#Raw

    lazy val label: String = s"${head.label} :&: ${tail.label}"
  }

  case object AnyPropertySet {
    /* Refiners */
    type withBound[B <: AnyProperty] = AnyPropertySet { type Properties <: AnyTypeSet.Of[B] }

    type withProperties[Ps <: AnyTypeSet.Of[AnyProperty]] = AnyPropertySet { type Properties = Ps }
    type withRaw[Vs <: AnyTypeSet] = AnyPropertySet { type Raw = Vs }
  }

  implicit def getPropertySetOps[PS <: AnyPropertySet](record: PS):
    PropertySetOps[PS] = PropertySetOps(record)

  case class PropertySetOps[PS <: AnyPropertySet](val propSet: PS) extends AnyVal {

    def :&:[P <: AnyProperty](p: P)(implicit check: P ∉ PS#Properties): (P :&: PS) = properties.:&:(p, propSet)
  }


  @annotation.implicitNotFound(msg = """
    Cannot prove that record
      ${PS}
    has property
      ${P}
  """)
  sealed class HasProperty[PS <: AnyPropertySet, P <: AnyProperty]

  case object HasProperty {

    implicit def pIsInProperties[PS <: AnyPropertySet, P <: AnyProperty]
      (implicit in: P ∈ PS#Properties):
          (PS HasProperty P) =
      new (PS HasProperty P)
  }

  @annotation.implicitNotFound(msg = """
    Cannot prove that record
      ${PS}
    has properties
      ${P}
  """)
  sealed class HasProperties[PS <: AnyPropertySet, P <: AnyTypeSet.Of[AnyProperty]]

  object HasProperties {
    import ops.typeSets.CheckForAll

    trait PropertyIsIn[PS <: AnyPropertySet] extends TypePredicate[AnyProperty] {
      type Condition[P <: AnyProperty] = PS HasProperty P
    }

    implicit def recordHasP[PS <: AnyPropertySet, P <: AnyTypeSet.Of[AnyProperty]]
      (implicit check: CheckForAll[P, PropertyIsIn[PS]]):
          (PS HasProperties P) =
      new (PS HasProperties P)
  }


}
