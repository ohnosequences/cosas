package ohnosequences.pointless

import AnyFn._

trait typeSet {

  /*
    ### ADT
  */
  type TypeSet
  type :~:[E,S <: TypeSet] <: TypeSet
  type ∅ <: TypeSet
  // the parser fails if I don't put a space before :
  val ∅ : ∅

  /*
    ### Predicates

    The basic predicates that you need for implementing the others are
  */
  protected type in[S <: TypeSet] <: {

    type    is[E]
    type isnot[E]
  }

  protected type subsetOf[Q <: TypeSet] <: {
    
    type    is[S <: TypeSet]
    type isnot[S <: TypeSet]
  }

  protected type supersetOf[Q <: TypeSet] = {

    type is[S <: TypeSet] = subsetOf[S]#is[Q]
    type isNot[S <: TypeSet] = subsetOf[S]#isnot[Q]
  }

  /*
    ### Proof witnesses
  */
  implicit def getIsIn[E: in[S]#is, S <: TypeSet]: (E ∈ S)  = new (E IsIn S)
  sealed class IsIn[E: in[S]#is, S <: TypeSet]
  final type ∈[E, S <: TypeSet] = IsIn[E,S]

  final type ∉[E, S <: TypeSet] = IsNotIn[E,S]
  implicit def getIsNotIn[E: in[S]#isnot, S <: TypeSet]: (E ∉ S) = new (E IsNotIn S)
  sealed class IsNotIn[E: in[S]#isnot, S <: TypeSet]
  
  implicit def getSubsetOf[U <: TypeSet: subsetOf[V]#is, V <: TypeSet]: (U ⊂ V) = new (U SubsetOf V)
  sealed class SubsetOf[U <: TypeSet: subsetOf[V]#is, V <: TypeSet]
  final type ⊂[U <: TypeSet, V <: TypeSet] = (U SubsetOf V)

  type ~[S <: TypeSet, Q <: TypeSet]
  type <<[S <: TypeSet, Q <: TypeSet]

  // functions
  type \[S <: TypeSet, Q <: TypeSet]            <: Fn2[S,Q] with WithCodomain[TypeSet]
  type ∪[S <: TypeSet, Q <: TypeSet]            <: Fn2[S,Q] with WithCodomain[TypeSet]

  type TakeFirstFrom[E, S <: TypeSet]           <: Fn2[E,S] with WithCodomain[E]

  type PopFrom[E, S <: TypeSet]                 <: Fn1[S] with WithCodomain[(E,TypeSet)]

  type TakeFrom[Q <: TypeSet, S <: TypeSet]     <: Fn2[Q,S] with Constant[Q]

  type ReplaceIn[Q <: TypeSet, S <: TypeSet]    <: Fn2[S,Q] with Constant[S]
  type As[S <: TypeSet, Q <: TypeSet]           <: Fn2[S,Q] with Constant[Q]

  import shapeless.{ HList, Poly }

  type SetMapper[F <: Poly, S <: TypeSet]       <: Fn2[F,S] with WithCodomain[TypeSet]

  // TODO review this one
  type SetMapFolder[F <: Poly, S <: TypeSet, R] <: Fn3[F,S,R] with Constant[R]

  type MapperToHList[F <: Poly, S <: TypeSet]     <: Fn2[F,S] with WithCodomain[HList]

  type MapperToList[F <: Poly, S <: TypeSet]      <: Fn2[F,S] with WrappedIn[List]

  type ToHList[S <: TypeSet]                    <: Fn1[S] with WithCodomain[HList]
  type ToList[S <: TypeSet]                     <: Fn1[S] with WrappedIn[List]
  final type ToListOf[S <: TypeSet, O0]         =  ToList[S] { type O = O0 }

  implicit def defaultOps[S <: TypeSet](set: S)(getOps: S => ops[S]): ops[S] = getOps(set)
  
  abstract class ops[S <: TypeSet](val set: S) {

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

    def take[Q <: TypeSet]
    (implicit 
      ev: Q ⊂ S,
      takeFrom: Q TakeFrom S
    )
    : takeFrom.Out

    def replace[Q <: TypeSet](q: Q)
    (implicit 
      ev: Q ⊂ S, 
      replaceIn: Q ReplaceIn S
    )
    : replaceIn.Out

    // = modulo order of types
    def as[Q <: TypeSet]
    (implicit 
      ev: S ~ Q,
      as: S As Q
    )
    : as.Out

    def as[Q <: TypeSet](q: Q)
    (implicit
      ev: S ~ Q,
      as: S As Q
    )
    : as.Out

    def \[Q <: TypeSet](q: Q)(implicit sub: S \ Q): sub.Out
    def ∪[Q <: TypeSet](q: Q)(implicit uni: S ∪ Q): uni.Out

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