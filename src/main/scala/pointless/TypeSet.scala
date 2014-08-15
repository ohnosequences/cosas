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
  @annotation.implicitNotFound(msg = "Can't prove that ${E} is an element of ${S}")
  type isIn[E, S <: AnyTypeSet]
  final type ∈[E, S <: AnyTypeSet] = E isIn S

  @annotation.implicitNotFound(msg = "Can't prove that ${E} is not an element of ${S}")
  type isNotIn[E, S <: AnyTypeSet]
  final type ∉[E, S <: AnyTypeSet] = E isNotIn S

  final type in[S <: AnyTypeSet] = {
    type    is[E] = E    isIn S
    type isNot[E] = E isNotIn S
  }

  /* One set is a subset of another */
  @annotation.implicitNotFound(msg = "Can't prove that ${Q} is a subset of ${Q}")
  type isSubsetOf[S <: AnyTypeSet, Q <: AnyTypeSet]
  final type ⊂[S <: AnyTypeSet, Q <: AnyTypeSet] = S isSubsetOf Q

  @annotation.implicitNotFound(msg = "Can't prove that ${Q} is not a subset of ${Q}")
  type isNotSubsetOf[S <: AnyTypeSet, Q <: AnyTypeSet]
  final type ⊄[S <: AnyTypeSet, Q <: AnyTypeSet] = S isNotSubsetOf Q

  final type subsetOf[Q <: AnyTypeSet] = {
    type    is[S <: AnyTypeSet] = S    isSubsetOf Q
    type isNot[S <: AnyTypeSet] = S isNotSubsetOf Q
  }


  /* Two sets have the same type union bound */
  @annotation.implicitNotFound(msg = "Can't prove that ${S} is the same as ${Q}")
  type isSameAs[S <: AnyTypeSet, Q <: AnyTypeSet]
  type ~:~[S <: AnyTypeSet, Q <: AnyTypeSet] = S isSameAs Q

  @annotation.implicitNotFound(msg = "Can't prove that ${S} is not the same as ${Q}")
  type isNotSameAs[S <: AnyTypeSet, Q <: AnyTypeSet]
  type ~:!~[S <: AnyTypeSet, Q <: AnyTypeSet] = S isNotSameAs Q

  final type sameAs[Q <: AnyTypeSet] = {
    type    is[S <: AnyTypeSet] = S    isSameAs Q
    type isNot[S <: AnyTypeSet] = S isNotSameAs Q
  }


  /* Elements of the set are bounded by the type */
  @annotation.implicitNotFound(msg = "Can't prove that elements of ${S} are bounded by ${B}")
  type isBoundedBy[S <: AnyTypeSet, B]

  @annotation.implicitNotFound(msg = "Can't prove that elements of ${S} are not bounded by ${B}")
  type isNotBoundedBy[S <: AnyTypeSet, B]

  final type boundedBy[B] = {
    type    is[S <: AnyTypeSet] = S    isBoundedBy B
    type isNot[S <: AnyTypeSet] = S isNotBoundedBy B
  }


  /* Elements of the set are from the type union */
  @annotation.implicitNotFound(msg = "Can't prove that elements of ${S} are from the type union ${U}")
  type isBoundedByUnion[S <: AnyTypeSet, U <: typeUnion#AnyTypeUnion]

  @annotation.implicitNotFound(msg = "Can't prove that elements of ${S} are not from the type union ${U}")
  type isNotBoundedByUnion[S <: AnyTypeSet, U <: typeUnion#AnyTypeUnion]

  final type boundedByUnion[U <: typeUnion#AnyTypeUnion] = {
    type    is[S <: AnyTypeSet] = S    isBoundedByUnion U
    type isNot[S <: AnyTypeSet] = S isNotBoundedByUnion U
  }


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


  abstract class AnyTypeSetOps[S <: AnyTypeSet](val set: S) {

    def :~:[E](e: E)(implicit check: E ∉ S): (E :~: S)

  //   def takeFirst[E]
  //   (implicit 
  //     ev: E ∈ S,
  //     takeFirstFrom: E TakeFirstFrom S
  //   )
  //   : takeFirstFrom.Out

  //   def pop[E]
  //   (implicit 
  //     ev: E ∈ S, 
  //     popFrom: E PopFrom S
  //   )
  //   : popFrom.Out

  //   def take[Q <: AnyTypeSet]
  //   (implicit 
  //     ev: Q ⊂ S,
  //     takeFrom: Q TakeFrom S
  //   )
  //   : takeFrom.Out

  //   def replace[Q <: AnyTypeSet](q: Q)
  //   (implicit 
  //     ev: Q ⊂ S, 
  //     replaceIn: Q ReplaceIn S
  //   )
  //   : replaceIn.Out

  //   // = modulo order of types
  //   def as[Q <: AnyTypeSet]
  //   (implicit 
  //     ev: S ~:~ Q,
  //     as: S As Q
  //   )
  //   : as.Out

  //   def as[Q <: AnyTypeSet](q: Q)
  //   (implicit
  //     ev: S ~:~ Q,
  //     as: S As Q
  //   )
  //   : as.Out

  //   def \[Q <: AnyTypeSet](q: Q)(implicit sub: S \ Q): sub.Out
  //   def ∪[Q <: AnyTypeSet](q: Q)(implicit uni: S ∪ Q): uni.Out

  //   import shapeless.Poly

  //   def map[F <: Poly](f: F)
  //   (implicit 
  //     setMapper: SetMapper[F,S]
  //   )
  //   : setMapper.Out

  //   def mapFold[F <: Poly, R](f: F)(r: R)(op: (R, R) => R)
  //   (implicit 
  //     setMapFolder: SetMapFolder[F,S,R]
  //   )
  //   : setMapFolder.Out

  //   def mapToHList[F <: Poly](f: F)
  //   (implicit 
  //     mapperToHList: MapperToHList[F,S]
  //   )
  //   : mapperToHList.Out

  //   def mapToList[F <: Poly](f: F)
  //   (implicit 
  //     mapperToList: MapperToList[F,S]
  //   )
  //   : mapperToList.Out

  //   // conversions
  //   def toHList
  //   (implicit 
  //     toHList: ToHList[S]
  //   )
  //   : toHList.Out

  //   def toList
  //   (implicit 
  //     toList: ToList[S]
  //   )
  //   : toList.Out

  //   def toListOf[O]
  //   (implicit 
  //     toListOf: S ToListOf O
  //   )
  //   : toListOf.Out

  }

}
