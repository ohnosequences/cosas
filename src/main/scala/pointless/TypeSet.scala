package ohnosequences.pointless

import AnyFn._, AnyTypeUnion._
import shapeless.{ HList, Poly1, <:!<, =:!= }

sealed trait AnyTypeSet {

  type Types <: AnyTypeUnion
  type Union // should be Types#union, but we can't set it here

  // This is a common bound for all elements (Union <: just[Bound])
  type Bound

  def toStr: String
  override def toString = "{" + toStr + "}"
}

trait TypeSetOf[B] extends AnyTypeSet { type Bound = B }

object TypeSet {
  type Of[T] = AnyTypeSet { type Bound = T }
}

trait AnyEmptySet extends AnyTypeSet {
  type Types = TypeUnion.empty
  type Union = Types#union

  def toStr = ""
}

object AnyEmptySet {
  type Of[T] = AnyEmptySet { type Bound <: T }
}

case class EmptySetOf[B]() extends AnyEmptySet with TypeSetOf[B] 

object EmptySet {
  type Of[T] = AnyEmptySet { type Bound = T }
}

import AnyTypeSet.{ ∉ }

trait NonEmptySet extends AnyTypeSet {
  type Types = Tail#Types#or[Head]
  type Union = Types#union
  
  type Tail <: AnyTypeSet
  val  tail: Tail

  // type Bound = Tail#Bound

  type Head <: Bound
  val  head: Head

  // should be provided implicitly:
  val headIsNew: Head ∉ Tail

  def toStr = {
    val h = head match {
      case _: String => "\""+head+"\""
      case _: Char   => "\'"+head+"\'"
      case _         => head.toString
    }
    val t = tail.toStr
    if (t.isEmpty) h else h+", "+t
  }
}

case class ConsSet[H <: T#Bound, T <: AnyTypeSet]
  (val head : H,  val tail : T)(implicit val headIsNew: H ∉ T) extends NonEmptySet with TypeSetOf[T#Bound] {
  type Head = H; type Tail = T
}

/* This method covers constructor to check that you are not adding a duplicate */
object ConsSet {
  def cons[H <: T#Bound, T <: AnyTypeSet](h: H, t: T)(implicit check: H ∉ T): ConsSet[H,T] = ConsSet(h, t) 
}

// object NonEmptySet {
//   type Of[T] = NonEmptySet {
//     type Union <: just[T]
//     type Head <: T
//     type Tail <: AnyTypeSet.Of[T]
//   }
// }

object AnyTypeSet {

  // def emptySetOf[B]: EmptySetOf[B] = new EmptySetOf[B]

  final type ∅[B] = EmptySetOf[B]
  def ∅[B]: ∅[B] = EmptySetOf[B]()

  // final type ∅ = EmptySetOf[Any]
  // val ∅ : ∅ = emptySetOf[Any] // the space before : is needed

  final type :~:[H <: T#Bound, T <: AnyTypeSet] = ConsSet[H, T]

  // it's like KList, but a set
  type Of[T] = AnyTypeSet { 
    // type Union <: just[T]
    type Bound <: T
  }

  // type withBound[T] = AnyTypeSet { 
  //   // type Union <: just[T]
  //   type Bound = T
  // }

  type SubsetOf[S <: AnyTypeSet] = AnyTypeSet { 
    type Union <: S#Union
    // type Bound <: S#Bound
  }

  type SupersetOf[S <: AnyTypeSet] = AnyTypeSet { 
    type Union >: S#Union 
    // type Bound >: S#Bound
  }

  type BoundedByUnion[U <: AnyTypeUnion] = AnyTypeSet { 
    type Union <: U#union 
    // type Bound <: U#union 
  }

  // NOTE: not sure that these function as expected, needs more testing
  // type SameAs[S <: AnyTypeSet] = SubsetOf[S] with SupersetOf[S]

  /*
    ### Predicates and aliases
  */

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
  type isSubsetOf[S <: AnyTypeSet, Q <: AnyTypeSet] = S#Union <:< Q#Union 
  @annotation.implicitNotFound(msg = "Can't prove that ${S} is a subset of ${Q}")
  final type ⊂[S <: AnyTypeSet, Q <: AnyTypeSet] = S isSubsetOf Q

  @annotation.implicitNotFound(msg = "Can't prove that ${S} is not a subset of ${Q}")
  type isNotSubsetOf[S <: AnyTypeSet, Q <: AnyTypeSet] = S#Union <:!< Q#Union
  @annotation.implicitNotFound(msg = "Can't prove that ${S} is not a subset of ${Q}")
  final type ⊄[S <: AnyTypeSet, Q <: AnyTypeSet] = S isNotSubsetOf Q

  final type subsetOf[Q <: AnyTypeSet] = {
    type    is[S <: AnyTypeSet] = S    isSubsetOf Q
    type isNot[S <: AnyTypeSet] = S isNotSubsetOf Q
  }


  /* Two sets have the same type union bound */
  @annotation.implicitNotFound(msg = "Can't prove that ${S} is the same as ${Q}")
  type    isSameAs[S <: AnyTypeSet, Q <: AnyTypeSet] = S#Union =:=  Q#Union
  type ~:~[S <: AnyTypeSet, Q <: AnyTypeSet] = S isSameAs Q

  @annotation.implicitNotFound(msg = "Can't prove that ${S} is not the same as ${Q}")
  type isNotSameAs[S <: AnyTypeSet, Q <: AnyTypeSet] = S#Union =:!= Q#Union
  type ~:!~[S <: AnyTypeSet, Q <: AnyTypeSet] = S isNotSameAs Q

  final type sameAs[Q <: AnyTypeSet] = {
    type    is[S <: AnyTypeSet] = S    isSameAs Q
    type isNot[S <: AnyTypeSet] = S isNotSameAs Q
  }


  /* Elements of the set are bounded by the type */
  @annotation.implicitNotFound(msg = "Can't prove that elements of ${S} are bounded by ${B}")
  type isBoundedBy[S <: AnyTypeSet, B] = S#Union <:< just[B]

  @annotation.implicitNotFound(msg = "Can't prove that elements of ${S} are not bounded by ${B}")
  type isNotBoundedBy[S <: AnyTypeSet, B] = S#Union <:!< just[B]

  final type boundedBy[B] = {
    type    is[S <: AnyTypeSet] = S    isBoundedBy B
    type isNot[S <: AnyTypeSet] = S isNotBoundedBy B
  }


  /* Elements of the set are from the type union */
  @annotation.implicitNotFound(msg = "Can't prove that elements of ${S} are from the type union ${U}")
  type    isBoundedByUnion[S <: AnyTypeSet, U <: AnyTypeUnion] = S#Union <:<  U#union

  @annotation.implicitNotFound(msg = "Can't prove that elements of ${S} are not from the type union ${U}")
  type isNotBoundedByUnion[S <: AnyTypeSet, U <: AnyTypeUnion] = S#Union <:!< U#union

  final type boundedByUnion[U <: AnyTypeUnion] = {
    type    is[S <: AnyTypeSet] = S    isBoundedByUnion U
    type isNot[S <: AnyTypeSet] = S isNotBoundedByUnion U
  }

  /* One set consists of representations of the types in another */
  @annotation.implicitNotFound(msg = "Can't prove that ${Vs} are values of ${Ws}")
  type areValuesOf[Vs <: AnyTypeSet, Ws <: AnyTypeSet.Of[AnyWrap]] = ops.typeSet.ValuesOf[Ws] { type Out = Vs }

  @annotation.implicitNotFound(msg = "Can't prove that ${Ws} are types of ${Vs}")
  type areWrapsOf[Ws <: AnyTypeSet, Vs <: AnyTypeSet.Of[AnyWrappedValue]] = ops.typeSet.WrapsOf[Vs] { type Out = Ws }

  type \[S <: AnyTypeSet, Q <: AnyTypeSet] = ops.typeSet.Subtract[S, Q]

  type ∪[S <: AnyTypeSet, Q <: AnyTypeSet] = ops.typeSet.Union[S, Q]

  type ToListOf[S <: AnyTypeSet, T] = ops.typeSet.ToList[S] { type O = T }


  implicit def typeSetOps[S <: AnyTypeSet](s: S): TypeSetOps[S] = new TypeSetOps[S](s)
  implicit def hListOps[L <: HList](l: L): HListOps[L] = new HListOps[L](l)

}

class TypeSetOps[S <: AnyTypeSet](val s: S) {
  import AnyTypeSet._
  import ops.typeSet._

  /* Element-related */

  def :~:[E <: S#Bound](e: E)(implicit check: E ∉ S): (E :~: S) = ConsSet.cons(e, s)

  def pop[E <: S#Bound](implicit pop: S Pop E): pop.Out = pop(s)

  def lookup[E <: S#Bound](implicit check: E ∈ S, lookup: S Lookup E): E = lookup(s)

  // deletes the first element of type E
  def delete[E <: S#Bound](implicit check: E ∈ S, del: S Delete E): del.Out = del(s)

  /* Set-related */

  def \[Q <: AnyTypeSet](q: Q)(implicit sub: S \ Q): sub.Out = sub(s, q)

  def ∪[Q <: AnyTypeSet](q: Q)(implicit uni: S ∪ Q): uni.Out = uni(s, q)

  def take[Q <: AnyTypeSet](implicit check: Q ⊂ S, take: S Take Q): take.Out = take(s)

  def replace[Q <: AnyTypeSet.Of[S#Bound]](q: Q)(implicit replace: S Replace Q): replace.Out = replace(s, q)


  /* Conversions */

  def reorderTo[Q <: AnyTypeSet]      (implicit check: S ~:~ Q, reorder: S ReorderTo Q): reorder.Out = reorder(s)
  def reorderTo[Q <: AnyTypeSet](q: Q)(implicit check: S ~:~ Q, reorder: S ReorderTo Q): reorder.Out = reorder(s)

  def toHList(implicit toHList: ToHList[S]): toHList.Out = toHList(s)

  def  toList(implicit  toList:  ToList[S]):  toList.Out =  toList(s)

  def toListOf[T](implicit toListOf: S ToListOf T): List[T] = toListOf(s)

  // def parseFrom[X](x: X)(implicit parser: S ParseFrom X): parser.Out = parser(s, x)

  def serializeTo[X](implicit serializer: S SerializeTo X): X = serializer(s)

  /* Mappers */

  def mapToHList[F <: Poly1](f: F)(implicit mapF: F MapToHList S): mapF.Out = mapF(s)

  def  mapToList[F <: Poly1](f: F)(implicit mapF: F  MapToList S): mapF.Out = mapF(s)

  def        map[F <: Poly1](f: F)(implicit mapF: F     MapSet S): mapF.Out = mapF(s)

  def mapFold[F <: Poly1, R](f: F)(r: R)(op: (R, R) => R)(implicit mapFold: MapFoldSet[F, S, R]): mapFold.Out = mapFold(s, r, op)

  
  // def aggregateProperties(implicit aggr: AggregateProperties[S]): aggr.Out = aggr(s)

  /* Predicates */

  def checkForAll[P <: AnyTypePredicate { type ArgBound >: S#Bound }](implicit prove: CheckForAll[S, P]): CheckForAll[S, P] = prove

  def checkForAny[P <: AnyTypePredicate { type ArgBound >: S#Bound }](implicit prove: CheckForAny[S, P]): CheckForAny[S, P] = prove
}

class HListOps[L <: HList](l: L) {
  def toTypeSet(implicit fromHList: ops.typeSet.FromHList[L]): fromHList.Out = fromHList(l)
}
