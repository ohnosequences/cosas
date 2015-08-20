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