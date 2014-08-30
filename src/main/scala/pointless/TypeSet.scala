package ohnosequences.pointless

import AnyFn._, AnyTypeUnion._, AnyTaggedType._
import shapeless.{ HList, Poly1, <:!<, =:!= }

sealed trait AnyTypeSet {

  type Types <: AnyTypeUnion
  type Bound // should be Types#union, but we can't set it here

  def toStr: String
  override def toString = "{" + toStr + "}"
}

trait EmptySet extends AnyTypeSet
trait NonEmptySet extends AnyTypeSet {
    type Head
    val  head: Head

    type Tail <: AnyTypeSet
    val  tail: Tail

    // should be provided implicitly:
    import AnyTypeSet.{ ∉ }
    val check: Head ∉ Tail
}

private[pointless] object TypeSetImpl {
  import AnyTypeSet._

  trait EmptySetImpl extends AnyTypeSet {

    type Types = either[Nothing]
    type Bound = Types#union

    def toStr = ""
  }

  object EmptySetImpl extends EmptySetImpl


  case class ConsSet[H, T <: AnyTypeSet]
    (val head : H,  val tail : T)(implicit val check: H ∉ T) extends NonEmptySet {
    type Head = H; type Tail = T

    type Types = Tail#Types#or[Head]
    type Bound = Types#union
    
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

  /* This method covers constructor to check that you are not adding a duplicate */
  object ConsSet {
    def cons[E, S <: AnyTypeSet](e: E, set: S)(implicit check: E ∉ S): ConsSet[E,S] = ConsSet(e, set) 
  }
}

object NonEmptySet {

  type Of[T] = NonEmptySet {
    type Bound <: just[T]
    type Head <: T
    type Tail <: AnyTypeSet.Of[T]
  }
}

object AnyTypeSet {

  final type ∅ = TypeSetImpl.EmptySetImpl
  val ∅ : ∅ = TypeSetImpl.EmptySetImpl // the space before : is needed
  val emptySet : ∅ = TypeSetImpl.EmptySetImpl

  final type :~:[E, S <: AnyTypeSet] = TypeSetImpl.ConsSet[E, S]

  // it's like KList, but a set
  type Of[T] = AnyTypeSet { type Bound <: just[T] }

  type SubsetOf[S <: AnyTypeSet] = AnyTypeSet { type Bound <: S#Bound }

  type SupersetOf[S <: AnyTypeSet] = AnyTypeSet { type Bound >: S#Bound }

  type BoundedByUnion[U <: AnyTypeUnion] = AnyTypeSet { type Bound <: U#union }

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
  type isSubsetOf[S <: AnyTypeSet, Q <: AnyTypeSet] = S#Types#union <:<  Q#Types#union 
  @annotation.implicitNotFound(msg = "Can't prove that ${S} is a subset of ${Q}")
  final type ⊂[S <: AnyTypeSet, Q <: AnyTypeSet] = S isSubsetOf Q

  @annotation.implicitNotFound(msg = "Can't prove that ${S} is not a subset of ${Q}")
  type isNotSubsetOf[S <: AnyTypeSet, Q <: AnyTypeSet] = S#Types#union <:!< Q#Types#union
  @annotation.implicitNotFound(msg = "Can't prove that ${S} is not a subset of ${Q}")
  final type ⊄[S <: AnyTypeSet, Q <: AnyTypeSet] = S isNotSubsetOf Q

  final type subsetOf[Q <: AnyTypeSet] = {
    type    is[S <: AnyTypeSet] = S    isSubsetOf Q
    type isNot[S <: AnyTypeSet] = S isNotSubsetOf Q
  }


  /* Two sets have the same type union bound */
  @annotation.implicitNotFound(msg = "Can't prove that ${S} is the same as ${Q}")
  type isSameAs[S <: AnyTypeSet, Q <: AnyTypeSet] = S#Types#union =:=  Q#Types#union
  type ~:~[S <: AnyTypeSet, Q <: AnyTypeSet] = S isSameAs Q

  @annotation.implicitNotFound(msg = "Can't prove that ${S} is not the same as ${Q}")
  type isNotSameAs[S <: AnyTypeSet, Q <: AnyTypeSet] = S#Types#union =:!= Q#Types#union
  type ~:!~[S <: AnyTypeSet, Q <: AnyTypeSet] = S isNotSameAs Q

  final type sameAs[Q <: AnyTypeSet] = {
    type    is[S <: AnyTypeSet] = S    isSameAs Q
    type isNot[S <: AnyTypeSet] = S isNotSameAs Q
  }


  /* Elements of the set are bounded by the type */
  @annotation.implicitNotFound(msg = "Can't prove that elements of ${S} are bounded by ${B}")
  type isBoundedBy[S <: AnyTypeSet, B] = S#Types#union <:<  either[B]#union

  @annotation.implicitNotFound(msg = "Can't prove that elements of ${S} are not bounded by ${B}")
  type isNotBoundedBy[S <: AnyTypeSet, B] = S#Types#union <:!< either[B]#union

  final type boundedBy[B] = {
    type    is[S <: AnyTypeSet] = S    isBoundedBy B
    type isNot[S <: AnyTypeSet] = S isNotBoundedBy B
  }


  /* Elements of the set are from the type union */
  @annotation.implicitNotFound(msg = "Can't prove that elements of ${S} are from the type union ${U}")
  type    isBoundedByUnion[S <: AnyTypeSet, U <: AnyTypeUnion] = S#Types#union <:<  U#union

  @annotation.implicitNotFound(msg = "Can't prove that elements of ${S} are not from the type union ${U}")
  type isNotBoundedByUnion[S <: AnyTypeSet, U <: AnyTypeUnion] = S#Types#union <:!< U#union

  final type boundedByUnion[U <: AnyTypeUnion] = {
    type    is[S <: AnyTypeSet] = S    isBoundedByUnion U
    type isNot[S <: AnyTypeSet] = S isNotBoundedByUnion U
  }

  /* One set consists of representations of the types in another */
  @annotation.implicitNotFound(msg = "Can't prove that ${S} is represented by ${R}")
  type isRepresentedBy[S <: AnyTypeSet, R <: AnyTypeSet] = ops.typeSet.Represented[S] with out[R]

  // type tagsOf[S <: AnyTypeSet, R <: AnyTypeSet] = ops.typeSet.TagsOf[S] with out[R]


  // object AnyTypeSet {
  /*
    ### Functions
  */
  type \[S <: AnyTypeSet, Q <: AnyTypeSet] = ops.typeSet.Subtract[S, Q]

  type ∪[S <: AnyTypeSet, Q <: AnyTypeSet] = ops.typeSet.Union[S, Q]

  // type Pop[S <: AnyTypeSet, E] = ops.typeSet.Pop[S, E]

  // type Lookup[S <: AnyTypeSet, E] = ops.typeSet.Lookup[S, E]

  // type Take[S <: AnyTypeSet, Q <: AnyTypeSet] = ops.typeSet.Take[S, Q]

  // type Replace[S <: AnyTypeSet, Q <: AnyTypeSet] = ops.typeSet.Replace[S, Q]

  // type ReorderTo[S <: AnyTypeSet, Q <: AnyTypeSet] = ops.typeSet.ReorderTo[S, Q]

  // type FromHList[L <: HList] = ops.typeSet.FromHList[L]

  // type ToHList[S <: AnyTypeSet] = ops.typeSet.ToHList[S]

  // type  ToList[S <: AnyTypeSet] = ops.typeSet.ToList[S]

  type ToListOf[S <: AnyTypeSet, T] = ops.typeSet.ToList[S] with o[T]

  // type MapToHList[F <: Poly1, S <: AnyTypeSet] = ops.typeSet.MapToHList[F, S]

  // type  MapToList[F <: Poly1, S <: AnyTypeSet] = ops.typeSet.MapToList[F, S]

  // type     MapSet[F <: Poly1, S <: AnyTypeSet] = ops.typeSet.MapSet[F, S]

  // type MapFoldSet[F <: Poly1, S <: AnyTypeSet, R] = ops.typeSet.MapFoldSet[F, S, R]

  implicit def typeSetOps[S <: AnyTypeSet](s: S): TypeSetOps[S] = new TypeSetOps[S](s)
  implicit def hListOps[L <: HList](l: L): HListOps[L] = new HListOps[L](l)

  def fromHList[L <: HList](l: L)
    (implicit fromHList: ops.typeSet.FromHList[L]): fromHList.Out = fromHList(l)

}


class TypeSetOps[S <: AnyTypeSet](s: S) {
  import AnyTypeSet._
  import ops.typeSet._

  /* Element-related */

  def :~:[E](e: E)(implicit check: E ∉ S): (E :~: S) = TypeSetImpl.ConsSet.cons(e, s)

  def pop[E](implicit check: E ∈ S, pop: S Pop E): pop.Out = pop(s)

  def lookup[E](implicit check: E ∈ S, lookup: S Lookup E): lookup.Out = lookup(s)


  /* Set-related */

  def \[Q <: AnyTypeSet](q: Q)(implicit sub: S \ Q): sub.Out = sub(s, q)

  def ∪[Q <: AnyTypeSet](q: Q)(implicit uni: S ∪ Q): uni.Out = uni(s, q)

  def take[Q <: AnyTypeSet](implicit check: Q ⊂ S, take: S Take Q): take.Out = take(s)

  def replace[Q <: AnyTypeSet](q: Q)(implicit check: Q ⊂ S, replace: S Replace Q): replace.Out = replace(s, q)


  /* Conversions */

  def reorderTo[Q <: AnyTypeSet]      (implicit check: S ~:~ Q, reorder: S ReorderTo Q): reorder.Out = reorder(s)
  def reorderTo[Q <: AnyTypeSet](q: Q)(implicit check: S ~:~ Q, reorder: S ReorderTo Q): reorder.Out = reorder(s)

  def toHList(implicit toHList: ToHList[S]): toHList.Out = toHList(s)

  def  toList(implicit  toList:  ToList[S]):  toList.Out =  toList(s)

  def toListOf[T](implicit toListOf: S ToListOf T): List[T] = toListOf(s)

  def parseFrom[X](x: X)(implicit parser: S ParseFrom X): parser.Out = parser(s, x)

  /* Mappers */

  def mapToHList[F <: Poly1](f: F)(implicit mapF: F MapToHList S): mapF.Out = mapF(s)

  def  mapToList[F <: Poly1](f: F)(implicit mapF: F  MapToList S): mapF.Out = mapF(s)

  def        map[F <: Poly1](f: F)(implicit mapF: F     MapSet S): mapF.Out = mapF(s)

  def mapFold[F <: Poly1, R](f: F)(r: R)(op: (R, R) => R)(implicit mapFold: MapFoldSet[F, S, R]): mapFold.Out = mapFold(s, r, op)

}

class HListOps[L <: HList](l: L) {
  def toTypeSet(implicit fromHList: ops.typeSet.FromHList[L]): fromHList.Out = fromHList(l)
}
