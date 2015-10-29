package ohnosequences.cosas

import types._, typeUnions._
import shapeless.HList

package object typeSets {

  final type ∅[+X] = EmptySet[X]
  def ∅[X] : ∅[X] = EmptySet[X](AnyEmptySet) // the space before : is needed
  // val emptySet : ∅ = TypeSetImpl.EmptySetImpl

  final type :~:[E <: S#Bound, S <: AnyTypeSet] = ConsSet[E, S]

  // functions
  type filter = Filter.type
  val filter: filter = Filter

  type union = Union.type
  val union: union = Union

  type lookup[E] = Lookup[E]
  def lookup[E]: lookup[E] = new Lookup[E]

  type pop[E] = Pop[E]
  def pop[X]: pop[X] = new Pop[X]

  type take[X <: AnyTypeSet] = Take[X]
  def take[X <: AnyTypeSet]: take[X] = new Take[X]

  type subtract = Subtract.type
  val subtract: subtract = Subtract

  type toListOf[X] = ToListOf[X]
  def toListOf[X]: toListOf[X] = new ToListOf[X]

  type mapToListOf[X] = MapToListOf[X]
  def mapToListOf[X]: mapToListOf[X] = new MapToListOf[X]

  type mapToHList = MapToHList.type
  val mapToHList: mapToHList = MapToHList

  type mapSet = MapSet.type
  val mapSet: mapSet = MapSet

  type toTypeMap[K <: AnyType, V] = ToTypeMap[K,V]
  def toTypeMap[K <: AnyType, V]: toTypeMap[K,V] = new ToTypeMap[K,V]

  type parseDenotations[Z, DS <: AnyTypeSet] = ParseDenotations[Z,DS]
  def parseDenotations[V, DS <: AnyTypeSet]: parseDenotations[V,DS] = new ParseDenotations[V,DS]

  type serializeDenotations[Z, DS <: AnyTypeSet] = SerializeDenotations[Z,DS]
  def serializeDenotations[V, DS <: AnyTypeSet]: serializeDenotations[V,DS] = new SerializeDenotations[V,DS]

  type reorderTo[Q <: AnyTypeSet] = ReorderTo[Q]
  def reorderTo[Q <: AnyTypeSet]: reorderTo[Q] = new ReorderTo[Q]

  type replace[Z <: AnyTypeSet] = Replace[Z]
  def replace[Q <: AnyTypeSet]: replace[Q] = new Replace[Q]


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
  type isSubsetOf[S <: AnyTypeSet, Q <: AnyTypeSet] = S#Union ≤ Q#Union
  @annotation.implicitNotFound(msg = "Can't prove that ${S} is a subset of ${Q}")
  final type ⊂[S <: AnyTypeSet, Q <: AnyTypeSet] = S isSubsetOf Q

  @annotation.implicitNotFound(msg = "Can't prove that ${S} is not a subset of ${Q}")
  type isNotSubsetOf[S <: AnyTypeSet, Q <: AnyTypeSet] = S#Union !< Q#Union
  @annotation.implicitNotFound(msg = "Can't prove that ${S} is not a subset of ${Q}")
  final type ⊄[S <: AnyTypeSet, Q <: AnyTypeSet] = S isNotSubsetOf Q

  final type subsetOf[Q <: AnyTypeSet] = {
    type    is[S <: AnyTypeSet] = S    isSubsetOf Q
    type isNot[S <: AnyTypeSet] = S isNotSubsetOf Q
  }

  /* Two sets have the same type union bound */
  @annotation.implicitNotFound(msg = "Can't prove that ${S} is the same as ${Q}")
  type    isSameAs[S <: AnyTypeSet, Q <: AnyTypeSet] = S#Union ≃ Q#Union
  type ~:~[S <: AnyTypeSet, Q <: AnyTypeSet] = S isSameAs Q

  @annotation.implicitNotFound(msg = "Can't prove that ${S} is not the same as ${Q}")
  type isNotSameAs[S <: AnyTypeSet, Q <: AnyTypeSet] = S#Union != Q#Union
  type ~:!~[S <: AnyTypeSet, Q <: AnyTypeSet] = S isNotSameAs Q

  final type sameAs[Q <: AnyTypeSet] = {
    type    is[S <: AnyTypeSet] = S    isSameAs Q
    type isNot[S <: AnyTypeSet] = S isNotSameAs Q
  }

  /* Elements of the set are bounded by the type */
  @annotation.implicitNotFound(msg = "Can't prove that elements of ${S} are bounded by ${B}")
  type isBoundedBy[S <: AnyTypeSet, B] = S#Bound ≤ B

  @annotation.implicitNotFound(msg = "Can't prove that elements of ${S} are not bounded by ${B}")
  type isNotBoundedBy[S <: AnyTypeSet, B] = S#Bound !< B

  final type boundedBy[B] = {
    type    is[S <: AnyTypeSet] = S    isBoundedBy B
    type isNot[S <: AnyTypeSet] = S isNotBoundedBy B
  }

  /* Elements of the set are from the type union */
  @annotation.implicitNotFound(msg = "Can't prove that elements of ${S} are from the type union ${U}")
  type    isBoundedByUnion[S <: AnyTypeSet, U <: AnyTypeUnion] = S#Union ≤ U#union

  @annotation.implicitNotFound(msg = "Can't prove that elements of ${S} are not from the type union ${U}")
  type isNotBoundedByUnion[S <: AnyTypeSet, U <: AnyTypeUnion] = S#Union !< U#union

  final type boundedByUnion[U <: AnyTypeUnion] = {
    type    is[S <: AnyTypeSet] = S    isBoundedByUnion U
    type isNot[S <: AnyTypeSet] = S isNotBoundedByUnion U
  }

  /* One set consists of representations of the types in another */
  // @annotation.implicitNotFound(msg = "Can't prove that ${Vs} are values of ${Ts}")
  // type areValuesOf[Vs <: AnyTypeSet, Ts <: AnyTypeSet] = typeSets.ValuesOf[Ts] { type Out = Vs }
  //
  // @annotation.implicitNotFound(msg = "Can't prove that ${Ts} are types of ${Vs}")
  // type areWrapsOf[Ts <: AnyTypeSet, Vs <: AnyTypeSet] = typeSets.WrapsOf[Ts] { type Out = Vs }

  // type \[S <: AnyTypeSet, Q <: AnyTypeSet] = typeSets.Subtract[S, Q]

  // type ∪[S <: AnyTypeSet, Q <: AnyTypeSet] = typeSets.Union[S, Q]

  // type ToListOf[S <: AnyTypeSet, T] = typeSets.ToList[S] { type O = T }
}
