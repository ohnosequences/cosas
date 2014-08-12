package ohnosequences.typesets.syntax

import ohnosequences.typesets.{ AnyFn, Fn1, Fn2, Fn3, Predicate }

trait Types { impl =>

  // the ADT
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
  type in[S <: TypeSet] <: {
    type    is[E]
    type isnot[E]
  }
  implicit def getIsIn[E: in[S]#is, S <: TypeSet] = new (E IsIn S)
  sealed class IsIn[E: in[S]#is, S <: TypeSet]
  type ∈[E, S <: TypeSet] = IsIn[E,S]

  type subsetOf[Q <: TypeSet] <: { 
    type    is[S <: TypeSet]
    type isnot[S <: TypeSet]
  }

  type supersetOf[Q <: TypeSet] = {

    type is[S <: TypeSet] = subsetOf[S]#is[Q]
    type isNot[S <: TypeSet] = subsetOf[S]#isnot[Q]
  }

  // predicates
  type notIn[E, X <: TypeSet]         <: Predicate

  

  type ⊂[S <: TypeSet, Q <: TypeSet]  <: Predicate
  type ~[S <: TypeSet, Q <: TypeSet]  <: Predicate
  type <<[S <: TypeSet, Q <: TypeSet] <: Predicate

  // functions
  type \[S <: TypeSet, Q <: TypeSet]          <: Fn2[S,Q] with AnyFn.withCodomain[TypeSet]
  type ∪[S <: TypeSet, Q <: TypeSet]          <: Fn2[S,Q] with AnyFn.withCodomain[TypeSet]

  type FirstOf[X <: TypeSet, Z] <: Fn2[X,Z]
  type Pop[S <: TypeSet, E] <: Fn1[S] with AnyFn.withCodomain[(E,TypeSet)]
  type Choose[S <: TypeSet, P <: TypeSet] <: Fn2[S,P] with AnyFn.constant[P]
  type Replace[S <: TypeSet, Q <: TypeSet] <: Fn2[S,Q] with AnyFn.constant[S]
  type Reorder[S <: TypeSet, Q <: TypeSet] <: Fn2[S,Q] with AnyFn.constant[Q]

  import shapeless.{ HList, Poly }

  type SetMapper[F <: Poly, S <: TypeSet] <: Fn2[F,S] with AnyFn.withCodomain[TypeSet]

  // TODO review this one
  type SetMapFolder[S <: TypeSet, F <: Poly, R] <: Fn3[S,F,R] with AnyFn.constant[R]

  type HListMapper[S <: TypeSet, F <: Poly] <: Fn2[S,F] with AnyFn.withCodomain[HList]
  type ListMapper[S <: TypeSet, F <: Poly] <: Fn2[S,F] { type Out <: List[_] }

  type ToHList[S <: TypeSet] <: Fn1[S] with AnyFn.withCodomain[HList]
  type ToList[S <: TypeSet] <: Fn1[S] { type Out <: List[_] }
  type ToListOf[S <: TypeSet, O0] = ToList[S] { type Out <: List[O0] } 
 
  implicit def toOps[
    Impl <: Syntax { type MyTypes = impl.type },
    X <: TypeSet
  ](x: X)(implicit 
    getImpl: X => Impl { type S = X }
  )
  : Impl = getImpl(x)
}

  trait Syntax {

    type MyTypes <: Types
    val types: MyTypes
    import types._
    
    type S <: types.TypeSet
    val set: S

    def :~:[E](e: E)(implicit n: types.notIn[E,S]): types.:~:[E,S]

    def lookup[U](implicit 
      ev: notIn[U,S],
      firstOf: FirstOf[S,U]
    ): firstOf.Out

    // def pop[E](implicit e: E ∈ S, p: Pop[S, E]): p.Out

    // def project[P <: TypeSet](implicit e: P ⊂ S, p: Choose[S, P]): p.Out

    // def replace[P <: TypeSet](p: P)(implicit e: P ⊂ S, r: Replace[S, P]): r.Out

    // def reorder[P <: TypeSet](implicit e: S ~ P, t: Reorder[S, P]): t.Out
    // def ~>[P <: TypeSet](p: P)(implicit e: S ~ P, t: Reorder[S, P]): t.Out

    // def \[Q <: TypeSet](q: Q)(implicit sub: S \ Q): sub.Out
    // def ∪[Q <: TypeSet](q: Q)(implicit uni: S ∪ Q): uni.Out

    // import shapeless.Poly

    // def map[F <: Poly](f: F)(implicit mapper: SetMapper[S, F]): mapper.Out

    // def mapHList[F <: Poly](f: F)(implicit mapper: HListMapper[S, F]): mapper.Out
    // def mapList[F <: Poly](f: F)(implicit mapper: ListMapper[S, F]): mapper.Out

    // def mapFold[F <: Poly, R](f: F)(r: R)(op: (R, R) => R)(implicit mapFolder: SetMapFolder[S,F,R]): mapFolder.Out

    // def toHList(implicit toHList: ToHList[S]): toHList.Out
    // def toList(implicit toList: ToList[S]): toList.Out
    // def toListOf[O](implicit toListOf: (S ToListOf O)): toListOf.Out
  }