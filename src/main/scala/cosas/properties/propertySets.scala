package ohnosequences.cosas.properties

import ohnosequences.cosas._, typeSets._, types._

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

case object HasProperties {

  trait PropertyIsIn[PS <: AnyPropertySet] extends TypePredicate[AnyProperty] {
    type Condition[P <: AnyProperty] = PS HasProperty P
  }

//   implicit def recordHasP[PS <: AnyPropertySet, P <: AnyTypeSet.Of[AnyProperty]]
//     (implicit check: CheckForAll[P, PropertyIsIn[PS]]):
//         (PS HasProperties P) =
//     new (PS HasProperties P)
}
