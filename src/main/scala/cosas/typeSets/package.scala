package ohnosequences.cosas

import types._, typeUnions._
import shapeless.{ HList, =:!=, <:!< }

package object typeSets {

  final type ∅ = TypeSetImpl.EmptySetImpl
  val ∅ : ∅ = TypeSetImpl.EmptySetImpl // the space before : is needed
  val emptySet : ∅ = TypeSetImpl.EmptySetImpl

  final type :~:[E, S <: AnyTypeSet] = TypeSetImpl.ConsSet[E, S]

  // functions
  def pop[E]: pop[E] = new pop[E]







  /* An element is in the set */
  @annotation.implicitNotFound(msg = "Can't prove that ${E} is an element of ${S}")
  final type isIn[E, S <: AnyTypeSet] = E isOneOf S#Types
  final type ∈[E, S <: AnyTypeSet] = E isIn S

  @annotation.implicitNotFound(msg = "Can't prove that ${E} is not an element of ${S}")
  type isNotIn[E, S <: AnyTypeSet] = E isNotOneOf S#Types
  final type ∉[E, S <: AnyTypeSet] = E isNotIn S

  final type in[S <: AnyTypeSet] = {
    type    is[E] = E    isIn S
    type isNot[E] = E isNotIn S
  }

  /* One set is a subset of another */
  @annotation.implicitNotFound(msg = "Can't prove that ${S} is a subset of ${Q}")
  type isSubsetOf[S <: AnyTypeSet, Q <: AnyTypeSet] = S#Bound <:< Q#Bound
  @annotation.implicitNotFound(msg = "Can't prove that ${S} is a subset of ${Q}")
  final type ⊂[S <: AnyTypeSet, Q <: AnyTypeSet] = S isSubsetOf Q

  @annotation.implicitNotFound(msg = "Can't prove that ${S} is not a subset of ${Q}")
  type isNotSubsetOf[S <: AnyTypeSet, Q <: AnyTypeSet] = S#Bound <:!< Q#Bound
  @annotation.implicitNotFound(msg = "Can't prove that ${S} is not a subset of ${Q}")
  final type ⊄[S <: AnyTypeSet, Q <: AnyTypeSet] = S isNotSubsetOf Q

  final type subsetOf[Q <: AnyTypeSet] = {
    type    is[S <: AnyTypeSet] = S    isSubsetOf Q
    type isNot[S <: AnyTypeSet] = S isNotSubsetOf Q
  }

  /* Two sets have the same type union bound */
  @annotation.implicitNotFound(msg = "Can't prove that ${S} is the same as ${Q}")
  type    isSameAs[S <: AnyTypeSet, Q <: AnyTypeSet] = S#Bound =:=  Q#Bound
  type ~:~[S <: AnyTypeSet, Q <: AnyTypeSet] = S isSameAs Q

  @annotation.implicitNotFound(msg = "Can't prove that ${S} is not the same as ${Q}")
  type isNotSameAs[S <: AnyTypeSet, Q <: AnyTypeSet] = S#Bound =:!= Q#Bound
  type ~:!~[S <: AnyTypeSet, Q <: AnyTypeSet] = S isNotSameAs Q

  final type sameAs[Q <: AnyTypeSet] = {
    type    is[S <: AnyTypeSet] = S    isSameAs Q
    type isNot[S <: AnyTypeSet] = S isNotSameAs Q
  }

  /* Elements of the set are bounded by the type */
  @annotation.implicitNotFound(msg = "Can't prove that elements of ${S} are bounded by ${B}")
  type isBoundedBy[S <: AnyTypeSet, B] = S#Bound <:< just[B]

  @annotation.implicitNotFound(msg = "Can't prove that elements of ${S} are not bounded by ${B}")
  type isNotBoundedBy[S <: AnyTypeSet, B] = S#Bound <:!< just[B]

  final type boundedBy[B] = {
    type    is[S <: AnyTypeSet] = S    isBoundedBy B
    type isNot[S <: AnyTypeSet] = S isNotBoundedBy B
  }

  /* Elements of the set are from the type union */
  @annotation.implicitNotFound(msg = "Can't prove that elements of ${S} are from the type union ${U}")
  type    isBoundedByUnion[S <: AnyTypeSet, U <: AnyTypeUnion] = S#Bound <:<  U#union

  @annotation.implicitNotFound(msg = "Can't prove that elements of ${S} are not from the type union ${U}")
  type isNotBoundedByUnion[S <: AnyTypeSet, U <: AnyTypeUnion] = S#Bound <:!< U#union

  final type boundedByUnion[U <: AnyTypeUnion] = {
    type    is[S <: AnyTypeSet] = S    isBoundedByUnion U
    type isNot[S <: AnyTypeSet] = S isNotBoundedByUnion U
  }

  /* One set consists of representations of the types in another */
  @annotation.implicitNotFound(msg = "Can't prove that ${Vs} are values of ${Ts}")
  type areValuesOf[Vs <: AnyTypeSet, Ts <: AnyTypeSet] = typeSets.ValuesOf[Ts] { type Out = Vs }

  @annotation.implicitNotFound(msg = "Can't prove that ${Ts} are types of ${Vs}")
  type areWrapsOf[Ts <: AnyTypeSet, Vs <: AnyTypeSet] = typeSets.WrapsOf[Ts] { type Out = Vs }

  type \[S <: AnyTypeSet, Q <: AnyTypeSet] = typeSets.Subtract[S, Q]

  type ∪[S <: AnyTypeSet, Q <: AnyTypeSet] = typeSets.Union[S, Q]

  type ToListOf[S <: AnyTypeSet, T] = typeSets.ToList[S] { type O = T }

  implicit def typeSetSyntax[S <: AnyTypeSet](s: S): syntax.TypeSetSyntax[S] =
    syntax.TypeSetSyntax(s)

  implicit def denotationsSetSyntax[DS <: AnyTypeSet.Of[AnyDenotation]](ds: DS): syntax.DenotationsSetSyntax[DS] =
    syntax.DenotationsSetSyntax(ds)

  implicit def hListSyntax[L <: HList](l: L): syntax.HListSyntax[L] =
    syntax.HListSyntax(l)
}
