package ohnosequences.cosas.properties

import ohnosequences.cosas._, typeSets._, types._

trait AnySetOfTypes extends AnyType {

  type Types <: AnyTypeSet.Of[AnyType]// of AnyType's
  val  types: Types

  type Raw <: AnyTypeSet.Of[AnyDenotation]

  type Size = Types#Size
}

case object EmptySetOfTypes extends AnySetOfTypes {

  type Types = ∅[AnyType]
  val  types: Types = ∅[AnyType]

  type Raw = ∅[AnyDenotation]

  val label: String = "□"
}

case class :&:[P <: AnyType, T <: AnySetOfTypes]
(val head: P, val tail: T)(implicit val headIsNew: P ∉ T#Types) extends AnySetOfTypes {

  type Types = P :~: T#Types
  val  types: Types = head :~: (tail.types: T#Types)

  type Raw = AnyDenotationOf[P] :~: T#Raw

  lazy val label: String = s"${head.label} :&: ${tail.label}"
}

case object AnySetOfTypes {
  /* Refiners */
  type withBound[B <: AnyType] = AnySetOfTypes { type Types <: AnyTypeSet.Of[B] }

  type withTypes[Ps <: AnyTypeSet.Of[AnyType]] = AnySetOfTypes { type Types = Ps }
  type withRaw[Vs <: AnyTypeSet] = AnySetOfTypes { type Raw = Vs }

  implicit def propertySetSyntax[PS <: AnySetOfTypes](record: PS): syntax.SetOfTypesSyntax[PS] =
    syntax.SetOfTypesSyntax(record)
}

@annotation.implicitNotFound(msg = """
  Cannot prove that record
    ${PS}
  has property
    ${P}
""")
sealed class HasType[PS <: AnySetOfTypes, P <: AnyType]

case object HasType {

  implicit def pIsInTypes[PS <: AnySetOfTypes, P <: AnyType]
    (implicit in: P ∈ PS#Types):
        (PS HasType P) =
    new (PS HasType P)
}

@annotation.implicitNotFound(msg = """
  Cannot prove that record
    ${PS}
  has properties
    ${P}
""")
sealed class HasTypes[PS <: AnySetOfTypes, P <: AnyTypeSet.Of[AnyType]]

case object HasTypes {

  trait TypeIsIn[PS <: AnySetOfTypes] extends TypePredicate[AnyType] {
    type Condition[P <: AnyType] = PS HasType P
  }

//   implicit def recordHasP[PS <: AnyTypeSet, P <: AnyTypeSet.Of[AnyType]]
//     (implicit check: CheckForAll[P, TypeIsIn[PS]]):
//         (PS HasTypes P) =
//     new (PS HasTypes P)
}
