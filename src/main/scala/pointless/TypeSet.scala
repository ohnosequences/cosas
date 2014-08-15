package ohnosequences.pointless

import AnyFn._

trait anyTypeSet {

  type typeUnion <: anyTypeUnion

  /*
    ### ADT
  */
  type AnyTypeSet

  type ∅ <: AnyTypeSet
  val  ∅ : ∅  // the space before : is needed

  type :~:[E,S <: AnyTypeSet] <: AnyTypeSet

  /*
    ### Predicates and aliases
  */

  /* An element is in the set */
  type in[S <: AnyTypeSet] <: PredicateOn[Any]

  @annotation.implicitNotFound(msg = "Can't prove that ${E} is an element of ${S}")
  sealed class isIn[E : in[S]#is, S <: AnyTypeSet]
  implicit def isIn[E : in[S]#is, S <: AnyTypeSet]: (E isIn S) = new (E isIn S)
  final type ∈[E, S <: AnyTypeSet] = E isIn S

  @annotation.implicitNotFound(msg = "Can't prove that ${E} is not an element of ${S}")
  sealed class isNotIn[E : in[S]#isNot, S <: AnyTypeSet]
  implicit def isNotIn[E : in[S]#isNot, S <: AnyTypeSet]: (E isNotIn S) = new (E isNotIn S)
  final type ∉[E, S <: AnyTypeSet] = E isNotIn S

  
  /* One set is a subset of another */
  type subsetOf[Q <: AnyTypeSet] <: PredicateOn[AnyTypeSet]

  @annotation.implicitNotFound(msg = "Can't prove that ${S} is a subset of ${Q}")
  sealed class isSubsetOf[S <: AnyTypeSet : subsetOf[Q]#is, Q <: AnyTypeSet]
  implicit def isSubsetOf[S <: AnyTypeSet : subsetOf[Q]#is, Q <: AnyTypeSet]: (S isSubsetOf Q) = new (S isSubsetOf Q)
  final type ⊂[S <: AnyTypeSet, Q <: AnyTypeSet] = S isSubsetOf Q

  @annotation.implicitNotFound(msg = "Can't prove that ${S} is not a subset of ${Q}")
  sealed class isNotSubsetOf[S <: AnyTypeSet : subsetOf[Q]#isNot, Q <: AnyTypeSet]
  implicit def isNotSubsetOf[S <: AnyTypeSet : subsetOf[Q]#isNot, Q <: AnyTypeSet]: (S isNotSubsetOf Q) = new (S isNotSubsetOf Q)
  final type ⊄[S <: AnyTypeSet, Q <: AnyTypeSet] = S isNotSubsetOf Q


  /* Two sets have the same type union bound */
  type sameAs[Q <: AnyTypeSet] <: PredicateOn[AnyTypeSet]

  @annotation.implicitNotFound(msg = "Can't prove that ${S} is the same as ${Q}")
  sealed class isSameAs[S <: AnyTypeSet : sameAs[Q]#is, Q <: AnyTypeSet]
  implicit def isSameAs[S <: AnyTypeSet : sameAs[Q]#is, Q <: AnyTypeSet]: (S isSameAs Q) = new (S isSameAs Q)
  final type ~:~[S <: AnyTypeSet, Q <: AnyTypeSet] = S isSameAs Q

  @annotation.implicitNotFound(msg = "Can't prove that ${S} is not the same as ${Q}")
  sealed class isNotSameAs[S <: AnyTypeSet : sameAs[Q]#isNot, Q <: AnyTypeSet]
  implicit def isNotSameAs[S <: AnyTypeSet : sameAs[Q]#isNot, Q <: AnyTypeSet]: (S isNotSameAs Q) = new (S isNotSameAs Q)
  final type ~!~[S <: AnyTypeSet, Q <: AnyTypeSet] = S isNotSameAs Q


  /* Elements of the set are bounded by the type */
  type boundedBy[B] <: PredicateOn[AnyTypeSet]

  @annotation.implicitNotFound(msg = "Can't prove that elements of ${S} are bounded by ${B}")
  sealed class isBoundedBy[S <: AnyTypeSet : boundedBy[B]#is, B]
  implicit def isBoundedBy[S <: AnyTypeSet : boundedBy[B]#is, B]: (S isBoundedBy B) = new (S isBoundedBy B)

  @annotation.implicitNotFound(msg = "Can't prove that elements of ${S} are not bounded by ${B}")
  sealed class isNotBoundedBy[S <: AnyTypeSet : boundedBy[B]#isNot, B]
  implicit def isNotBoundedBy[S <: AnyTypeSet : boundedBy[B]#isNot, B]: (S isNotBoundedBy B) = new (S isNotBoundedBy B)


  /* Elements of the set are from the type union */
  type boundedByUnion[U <: typeUnion#AnyTypeUnion] <: PredicateOn[AnyTypeSet]

  @annotation.implicitNotFound(msg = "Can't prove that elements of ${S} are from the type union ${U}")
  sealed class isBoundedByUnion[S <: AnyTypeSet : boundedByUnion[U]#is, U <: typeUnion#AnyTypeUnion]
  implicit def isBoundedByUnion[S <: AnyTypeSet : boundedByUnion[U]#is, U <: typeUnion#AnyTypeUnion]: (S isBoundedByUnion U) = new (S isBoundedByUnion U)

  @annotation.implicitNotFound(msg = "Can't prove that elements of ${S} are not from the type union ${U}")
  sealed class isNotBoundedByUnion[S <: AnyTypeSet : boundedByUnion[U]#isNot, U <: typeUnion#AnyTypeUnion]
  implicit def isNotBoundedByUnion[S <: AnyTypeSet : boundedByUnion[U]#isNot, U <: typeUnion#AnyTypeUnion]: (S isNotBoundedByUnion U) = new (S isNotBoundedByUnion U)



  /*
    ### Functions
  */
  type \[S <: AnyTypeSet, Q <: AnyTypeSet] <: Fn2[S,Q] with WithCodomain[AnyTypeSet]
  type ∪[S <: AnyTypeSet, Q <: AnyTypeSet] <: Fn2[S,Q] with WithCodomain[AnyTypeSet]

  type TakeFirstFrom[E, S <: AnyTypeSet] <: Fn2[E,S] with WithCodomain[E]

  type PopFrom[E, S <: AnyTypeSet] <: Fn1[S] with WithCodomain[(E,AnyTypeSet)]

  type TakeFrom[Q <: AnyTypeSet, S <: AnyTypeSet] <: Fn2[Q,S] with Constant[Q]

  type ReplaceIn[Q <: AnyTypeSet, S <: AnyTypeSet] <: Fn2[S,Q] with Constant[S]
  type        As[S <: AnyTypeSet, Q <: AnyTypeSet] <: Fn2[S,Q] with Constant[Q]

  import shapeless.{ HList, Poly }

  type SetMapper[F <: Poly, S <: AnyTypeSet] <: Fn2[F,S] with WithCodomain[AnyTypeSet]

  // TODO review this one
  type SetMapFolder[F <: Poly, S <: AnyTypeSet, R] <: Fn3[F,S,R] with Constant[R]

  type MapperToHList[F <: Poly, S <: AnyTypeSet] <: Fn2[F,S] with WithCodomain[HList]

  type MapperToList[F <: Poly, S <: AnyTypeSet] <: Fn2[F,S] with WrappedIn[List]

  type ToHList[S <: AnyTypeSet] <: Fn1[S] with WithCodomain[HList]
  type  ToList[S <: AnyTypeSet] <: Fn1[S] with WrappedIn[List]
  final type ToListOf[S <: AnyTypeSet, O0] = ToList[S] { type O = O0 }

  implicit def defaultOps[S <: AnyTypeSet](set: S)(getOps: S => ops[S]): ops[S] = getOps(set)
  
  abstract class ops[S <: AnyTypeSet](val set: S) {

    def :~:[E](e: E)(implicit ev: E ∉ S): (E :~: S)

    def takeFirst[E]
    (implicit 
      ev: E ∈ S,
      takeFirstFrom: E TakeFirstFrom S
    )
    : takeFirstFrom.Out

    def pop[E]
    (implicit 
      ev: E ∈ S, 
      popFrom: E PopFrom S
    )
    : popFrom.Out

    def take[Q <: AnyTypeSet]
    (implicit 
      ev: Q ⊂ S,
      takeFrom: Q TakeFrom S
    )
    : takeFrom.Out

    def replace[Q <: AnyTypeSet](q: Q)
    (implicit 
      ev: Q ⊂ S, 
      replaceIn: Q ReplaceIn S
    )
    : replaceIn.Out

    // = modulo order of types
    def as[Q <: AnyTypeSet]
    (implicit 
      ev: S ~:~ Q,
      as: S As Q
    )
    : as.Out

    def as[Q <: AnyTypeSet](q: Q)
    (implicit
      ev: S ~:~ Q,
      as: S As Q
    )
    : as.Out

    def \[Q <: AnyTypeSet](q: Q)(implicit sub: S \ Q): sub.Out
    def ∪[Q <: AnyTypeSet](q: Q)(implicit uni: S ∪ Q): uni.Out

    import shapeless.Poly

    def map[F <: Poly](f: F)
    (implicit 
      setMapper: SetMapper[F,S]
    )
    : setMapper.Out

    def mapFold[F <: Poly, R](f: F)(r: R)(op: (R, R) => R)
    (implicit 
      setMapFolder: SetMapFolder[F,S,R]
    )
    : setMapFolder.Out

    def mapToHList[F <: Poly](f: F)
    (implicit 
      mapperToHList: MapperToHList[F,S]
    )
    : mapperToHList.Out

    def mapToList[F <: Poly](f: F)
    (implicit 
      mapperToList: MapperToList[F,S]
    )
    : mapperToList.Out

    // conversions
    def toHList
    (implicit 
      toHList: ToHList[S]
    )
    : toHList.Out

    def toList
    (implicit 
      toList: ToList[S]
    )
    : toList.Out

    def toListOf[O]
    (implicit 
      toListOf: S ToListOf O
    )
    : toListOf.Out
  }

}
