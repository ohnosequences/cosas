package ohnosequences.cosas

object typeSets {

  // deps
  import typeUnions._
  import shapeless.{ HList, Poly1, <:!<, =:!= }

  import shapeless.Nat._
  import shapeless.{Nat, Succ}

  sealed trait AnyTypeSet {

    type Types <: AnyTypeUnion
    type Bound // should be Types#union, but we can't set it here

    type Size <: Nat

    def toStr: String
    override def toString = "{" + toStr + "}"
  }

  trait EmptySet extends AnyTypeSet { type Size = _0 }
  trait NonEmptySet extends AnyTypeSet {

      type Head
      val  head: Head

      type Tail <: AnyTypeSet
      val  tail: Tail

      type Size = Succ[Tail#Size]
      // should be provided implicitly:
      val headIsNew: Head ∉ Tail
  }

  private[cosas] object TypeSetImpl {

    trait EmptySetImpl extends AnyTypeSet with EmptySet {

      type Types = empty
      type Bound = Types#union

      def toStr = ""
    }

    object EmptySetImpl extends EmptySetImpl { override type Types = empty }


    case class ConsSet[H, T <: AnyTypeSet]
      (val head : H,  val tail : T)(implicit val headIsNew: H ∉ T) extends NonEmptySet {
      type Head = H; type Tail = T

      type Types = Tail#Types or Head
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

  type size[S <: AnyTypeSet] = S#Size

  final type ∅ = TypeSetImpl.EmptySetImpl
  val ∅ : ∅ = TypeSetImpl.EmptySetImpl // the space before : is needed
  val emptySet : ∅ = TypeSetImpl.EmptySetImpl

  final type :~:[E, S <: AnyTypeSet] = TypeSetImpl.ConsSet[E, S]

  // it's like KList, but a set
  object AnyTypeSet {
    type Of[T] = AnyTypeSet { type Bound <: just[T] }

    type SubsetOf[S <: AnyTypeSet] = AnyTypeSet { type Bound <: S#Bound }

    type SupersetOf[S <: AnyTypeSet] = AnyTypeSet { type Bound >: S#Bound }

    type BoundedByUnion[U <: AnyTypeUnion] = AnyTypeSet { type Bound <: U#union }

    // type SameAs[S <: AnyTypeSet] = SubsetOf[S] with SupersetOf[S]

    implicit def typeSetOps[S <: AnyTypeSet](s: S): TypeSetOps[S] = new TypeSetOps[S](s)
  }

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
  type areValuesOf[Vs <: AnyTypeSet, Ts <: AnyTypeSet] = ops.typeSets.ValuesOf[Ts] { type Out = Vs }

  @annotation.implicitNotFound(msg = "Can't prove that ${Ts} are types of ${Vs}")
  type areWrapsOf[Ts <: AnyTypeSet, Vs <: AnyTypeSet] = ops.typeSets.WrapsOf[Ts] { type Out = Vs }

  type \[S <: AnyTypeSet, Q <: AnyTypeSet] = ops.typeSets.Subtract[S, Q]

  type ∪[S <: AnyTypeSet, Q <: AnyTypeSet] = ops.typeSets.Union[S, Q]

  type ToListOf[S <: AnyTypeSet, T] = ops.typeSets.ToList[S] { type O = T }


  trait AnyTypePredicate {

    type ArgBound
    type Condition[X <: ArgBound]
  }

  trait TypePredicate[B] extends AnyTypePredicate { type ArgBound = B }

  type Accepts[P <: AnyTypePredicate, X <: P#ArgBound] = P#Condition[X]


  class TypeSetOps[S <: AnyTypeSet](val s: S) {
    import ops.typeSets._

    /* Element-related */

    def :~:[E](e: E)(implicit check: E ∉ S): (E :~: S) = TypeSetImpl.ConsSet.cons(e, s) : (E :~: S)

    def pop[E](implicit pop: S Pop E): pop.Out = pop(s)

    def lookup[E](implicit check: E ∈ S, lookup: S Lookup E): E = lookup(s)

    // deletes the first element of type E
    def delete[E](implicit check: E ∈ S, del: S Delete E): del.Out = del(s)

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

    def toMap[K, V](implicit toMap: ToMap[S, K, V]): Map[K, V] = toMap(s)

    def parseFrom[X](x: X)(implicit parser: S ParseFrom X): parser.Out = parser(s, x)

    def serializeTo[X](implicit serializer: S SerializeTo X): X = serializer(s)

    def getTypes[X](implicit types: TypesOf[S] { type Out = X }): X = types(s)

    /* Mappers */

    def mapToHList[F <: Poly1](f: F)(implicit mapF: F MapToHList S): mapF.Out = mapF(s)

    def  mapToList[F <: Poly1](f: F)(implicit mapF: F  MapToList S): mapF.Out = mapF(s)

    def        map[F <: Poly1](f: F)(implicit mapF: F     MapSet S): mapF.Out = mapF(s)

    def mapFold[F <: Poly1, R](f: F)(r: R)(op: (R, R) => R)(implicit mapFold: MapFoldSet[F, S, R]): mapFold.Out = mapFold(s, r, op)

    /* Predicates */

    def filter[P <: AnyTypePredicate](implicit fltr: Filter[S, P]): fltr.Out = fltr(s)

    def checkForAll[P <: AnyTypePredicate](implicit prove: CheckForAll[S, P]): CheckForAll[S, P] = prove

    def checkForAny[P <: AnyTypePredicate](implicit prove: CheckForAny[S, P]): CheckForAny[S, P] = prove
  }


  implicit def hListOps[L <: HList](l: L): HListOps[L] = new HListOps[L](l)

  class HListOps[L <: HList](l: L) {
    def toTypeSet(implicit fromHList: ops.typeSets.FromHList[L]): fromHList.Out = fromHList(l)
  }

}
