package ohnosequences.pointless

import AnyFn._

trait anyTypeSet {

  /*
    ### ADT
  */
  type AnyTypeSet

  type ∅ <: AnyTypeSet
  val  ∅ : ∅  // the parser fails if without the space before :

  type :~:[E,S <: AnyTypeSet] <: AnyTypeSet

  /*
    ### Predicates

    The basic predicates that you need for implementing the others are
  */
  type in[S <: AnyTypeSet] <: PredicateOn[Any]
  type subsetOf[Q <: AnyTypeSet] <: PredicateOn[AnyTypeSet]
  type sameAs[Q <: AnyTypeSet] <: PredicateOn[AnyTypeSet]

  /*
    ### Proof witnesses
  */
  final type ∈[E, S <: AnyTypeSet] = IsIn[E,S]
  // what's the point of this IsIn anyway? why not just `sealed class ∈[...]`?
  protected sealed class IsIn[E: in[S]#is, S <: AnyTypeSet]
  implicit def      proveIsIn[E: in[S]#is, S <: AnyTypeSet]: (E ∈ S) = new (E IsIn S)

  final type ∉[E, S <: AnyTypeSet] = IsNotIn[E,S]
  protected sealed class IsNotIn[E: in[S]#isNot, S <: AnyTypeSet]
  implicit def      proveIsNotIn[E: in[S]#isNot, S <: AnyTypeSet]: (E ∉ S) = new (E IsNotIn S)
  
  final type ⊂[S <: AnyTypeSet, Q <: AnyTypeSet] = (S SubsetOf Q)
  protected sealed class SubsetOf[S <: AnyTypeSet: subsetOf[Q]#is, Q <: AnyTypeSet]
  implicit def      proveSubsetOf[S <: AnyTypeSet: subsetOf[Q]#is, Q <: AnyTypeSet]: (S ⊂ Q) = new (S SubsetOf Q)

  final type ~[S <: AnyTypeSet, Q <: AnyTypeSet] = S SameAs Q
  protected sealed class SameAs[S <: AnyTypeSet: sameAs[Q]#is, Q <: AnyTypeSet]
  implicit def      proveSameAs[S <: AnyTypeSet: sameAs[Q]#is, Q <: AnyTypeSet]: (S ~ Q) = new (S SameAs Q)

  type <<[S <: AnyTypeSet, Q <: AnyTypeSet]

  // functions
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
      ev: S ~ Q,
      as: S As Q
    )
    : as.Out

    def as[Q <: AnyTypeSet](q: Q)
    (implicit
      ev: S ~ Q,
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
