package ohnosequences.pointless

import AnyFn._
import shapeless.{ HList, Poly1 }

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
  @annotation.implicitNotFound(msg = "Popping is not implemented")
  type Pop[S <: AnyTypeSet, E] <: Fn1[S] with WithCodomain[(E,AnyTypeSet)]

  @annotation.implicitNotFound(msg = "Lookup is not implemented")
  type Lookup[S <: AnyTypeSet, E] <: Fn1[S] with WithCodomain[E]

  @annotation.implicitNotFound(msg = "Subtraction is not implemented")
  type \[S <: AnyTypeSet, Q <: AnyTypeSet] <: Fn2[S,Q] with WithCodomain[AnyTypeSet]

  @annotation.implicitNotFound(msg = "Union is not implemented")
  type ∪[S <: AnyTypeSet, Q <: AnyTypeSet] <: Fn2[S,Q] with WithCodomain[AnyTypeSet]

  @annotation.implicitNotFound(msg = "Projection (Take) is not implemented")
  type Take[S <: AnyTypeSet, Q <: AnyTypeSet] <: Fn1[S] with Constant[Q]

  @annotation.implicitNotFound(msg = "Replacing is not implemented")
  type Replace[S <: AnyTypeSet, Q <: AnyTypeSet] <: Fn2[S, Q] with Constant[S]

  @annotation.implicitNotFound(msg = "Reordering is not implemented")
  type As[S <: AnyTypeSet, Q <: AnyTypeSet] <: Fn1[S] with Constant[Q]

  @annotation.implicitNotFound(msg = "Conversion to HList is not implemented")
  type ToHList[S <: AnyTypeSet] <: Fn1[S] with WithCodomain[HList]

  @annotation.implicitNotFound(msg = "Conversion to List is not implemented")
  type  ToList[S <: AnyTypeSet] <: Fn1[S] with WrappedIn[List]

  @annotation.implicitNotFound(msg = "Conversion to List is not implemented")
  final type ToListOf[S <: AnyTypeSet, T] = ToList[S] with o[T]

  @annotation.implicitNotFound(msg = "Mapping to HList is not implemented")
  type MapToHList[F <: Poly1, S <: AnyTypeSet] <: Fn1[S] with WithCodomain[HList]

  @annotation.implicitNotFound(msg = "Mapping to List is not implemented")
  type MapToList[F <: Poly1, S <: AnyTypeSet] <: Fn1[S] with WrappedIn[List]

  @annotation.implicitNotFound(msg = "Mapping is not implemented")
  type MapSet[F <: Poly1, S <: AnyTypeSet] <: Fn1[S] with WithCodomain[AnyTypeSet]

  @annotation.implicitNotFound(msg = "Map-folding is not implemented")
  type MapFoldSet[F <: Poly1, S <: AnyTypeSet, R] <: Fn3[S, R, (R, R) => R] with Constant[R]


  abstract class AnyTypeSetOps[S <: AnyTypeSet](s: S) {

    /* Element-related */

    def :~:[E](e: E)(implicit check: E ∉ S): (E :~: S)

    def pop[E](implicit check: E ∈ S, pop: S Pop E): pop.Out = pop(s)

    def lookup[E](implicit check: E ∈ S, lookup: S Lookup E): lookup.Out = lookup(s)


    /* Set-related */

    def \[Q <: AnyTypeSet](q: Q)(implicit sub: S \ Q): sub.Out = sub(s, q)

    def ∪[Q <: AnyTypeSet](q: Q)(implicit uni: S ∪ Q): uni.Out = uni(s, q)

    def take[Q <: AnyTypeSet](implicit check: Q ⊂ S, take: S Take Q): take.Out = take(s)

    def replace[Q <: AnyTypeSet](q: Q)(implicit check: Q ⊂ S, replace: S Replace Q): replace.Out = replace(s, q)


    /* Conversions */

    def as[Q <: AnyTypeSet]      (implicit check: S ~:~ Q, reorder: S As Q): reorder.Out = reorder(s)
    def as[Q <: AnyTypeSet](q: Q)(implicit check: S ~:~ Q, reorder: S As Q): reorder.Out = reorder(s)

    def toHList(implicit toHList: ToHList[S]): toHList.Out = toHList(s)

    def  toList(implicit  toList:  ToList[S]):  toList.Out =  toList(s)

    def toListOf[T](implicit toListOf: S ToListOf T): List[T] = toListOf(s)


    /* Mappers */

    def mapToHList[F <: Poly1](f: F)(implicit mapF: F MapToHList S): mapF.Out = mapF(s)

    def  mapToList[F <: Poly1](f: F)(implicit mapF: F  MapToList S): mapF.Out = mapF(s)

    def        map[F <: Poly1](f: F)(implicit mapF: F     MapSet S): mapF.Out = mapF(s)

    def mapFold[F <: Poly1, R](f: F)(r: R)(op: (R, R) => R)(implicit mapFold: MapFoldSet[F, S, R]): mapFold.Out = mapFold(s, r, op)

  }

}
