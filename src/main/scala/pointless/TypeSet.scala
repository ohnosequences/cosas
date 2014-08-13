package ohnosequences.pointless

import AnyFn._

trait typeSet extends Module {

  trait types extends Types {

    // the ADT; an alias for convenience
    final type Carrier = TypeSet

    type TypeSet
    type :~:[E,S <: TypeSet] <: TypeSet
    type ∅ <: TypeSet
    // the parser fails if I don't put a space before :
    val ∅ : ∅

    /*
    The basic predicates that you need for implementing the others are
    */
      
    /*
      an instance of 
    */
    protected type in[S <: TypeSet] <: {

      type    is[E]
      type isnot[E]
    }

    type subsetOf[Q <: TypeSet] <: {
      
      type    is[S <: TypeSet]
      type isnot[S <: TypeSet]
    }

    type supersetOf[Q <: TypeSet] = {

      type is[S <: TypeSet] = subsetOf[S]#is[Q]
      type isNot[S <: TypeSet] = subsetOf[S]#isnot[Q]
    }

    type ~[S <: TypeSet, Q <: TypeSet]
    type <<[S <: TypeSet, Q <: TypeSet]

    // functions
    type \[S <: TypeSet, Q <: TypeSet]            <: Fn2[S,Q] with WithCodomain[TypeSet]
    type ∪[S <: TypeSet, Q <: TypeSet]            <: Fn2[S,Q] with WithCodomain[TypeSet]

    type FirstOf[X <: TypeSet, Z]                 <: Fn2[X,Z]
    type Pop[S <: TypeSet, E]                     <: Fn1[S] with WithCodomain[(E,TypeSet)]
    type Choose[S <: TypeSet, P <: TypeSet]       <: Fn2[S,P] with Constant[P]
    type Replace[S <: TypeSet, Q <: TypeSet]      <: Fn2[S,Q] with Constant[S]
    type Reorder[S <: TypeSet, Q <: TypeSet]      <: Fn2[S,Q] with Constant[Q]

    import shapeless.{ HList, Poly }

    type SetMapper[F <: Poly, S <: TypeSet]       <: Fn2[F,S] with WithCodomain[TypeSet]

    // TODO review this one
    type SetMapFolder[S <: TypeSet, F <: Poly, R] <: Fn3[S,F,R] with Constant[R]

    type HListMapper[S <: TypeSet, F <: Poly]     <: Fn2[S,F] with WithCodomain[HList]

    type ListMapper[S <: TypeSet, F <: Poly]      <: Fn2[S,F] with WrappedIn[List]

    type ToHList[S <: TypeSet]                    <: Fn1[S] with WithCodomain[HList]
    type ToList[S <: TypeSet]                     <: Fn1[S] with WrappedIn[List]
    final type ToListOf[S <: TypeSet, O0]         =  ToList[S] { type O = O0 }


    /*
      ### proofs
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

    implicit def defaultOps[TS <: TypeSet](set: TS)(getOps: TS => ops[TS]): ops[TS] = getOps(set)
    
    abstract class ops[S <: TypeSet](val set: S) extends Ops[S](set) {

      def :~:[E](e: E)(implicit ev: (E ∉ S)): (E :~: S)

      def lookup[E](implicit 
        ev: E ∈ S,
        firstOf: S FirstOf E
      ): firstOf.Out

      def pop[V](implicit ev: (V ∈ S), p: Pop[S, V]): p.Out

      def project[P <: TypeSet](implicit e: P ⊂ S, p: (S Choose P)): p.Out

      def replace[P <: TypeSet](p: P)(implicit e: P ⊂ S, r: Replace[S, P]): r.Out

      def reorder[P <: TypeSet](implicit e: S ~ P, t: Reorder[S, P]): t.Out
      def ~>[P <: TypeSet](p: P)(implicit e: S ~ P, t: Reorder[S, P]): t.Out

      def \[Q <: TypeSet](q: Q)(implicit sub: S \ Q): sub.Out
      def ∪[Q <: TypeSet](q: Q)(implicit uni: S ∪ Q): uni.Out

      import shapeless.Poly

      def map[F <: Poly](f: F)(implicit mapper: SetMapper[F,S]): mapper.Out

      def mapHList[F <: Poly](f: F)(implicit mapper: HListMapper[S, F]): mapper.Out
      def mapList[F <: Poly](f: F)(implicit mapper: ListMapper[S, F]): mapper.Out

      def mapFold[F <: Poly, R](f: F)(r: R)(op: (R, R) => R)(implicit mapFolder: SetMapFolder[S,F,R]): mapFolder.Out

      def toHList(implicit toHList: ToHList[S]): toHList.Out
      def toList(implicit toList: ToList[S]): toList.Out
      def toListOf[O](implicit toListOf: (S ToListOf O)): toListOf.Out
    }

  }
}