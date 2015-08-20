package ohnosequences.cosas.ops.records

import ohnosequences.cosas._, types._, records._, properties._, fns._, typeSets._
import ops.typeSets.CheckForAll

@annotation.implicitNotFound(msg = """
  Cannot prove that record
    ${R}
  has property
    ${P}
""")
sealed class HasProperty[R <: AnyFields, P <: AnyProperty]

case object HasProperty {

  implicit def pIsInProperties[R <: AnyFields, P <: AnyProperty]
    (implicit in: P ∈ R#Properties):
        (R HasProperty P) =
    new (R HasProperty P)
}

@annotation.implicitNotFound(msg = """
  Cannot prove that record
    ${R}
  has properties
    ${Ps}
""")
sealed class HasProperties[R <: AnyFields, Ps <: AnyTypeSet.Of[AnyProperty]]

object HasProperties {

  trait PropertyIsIn[R <: AnyFields] extends TypePredicate[AnyProperty] {
    type Condition[P <: AnyProperty] = R HasProperty P
  }

  implicit def recordHasPs[R <: AnyFields, Ps <: AnyTypeSet.Of[AnyProperty]]
    (implicit check: CheckForAll[Ps, PropertyIsIn[R]]):
        (R HasProperties Ps) =
    new (R HasProperties Ps)
}
