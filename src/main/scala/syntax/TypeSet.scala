package ohnosequences.typesets.syntax

import ohnosequences.typesets.{ AnyFn, Fn1, Fn2, Fn3, Predicate }

trait Types {

    // the ADT
    type Carrier
    type :~:[E,S <: Carrier] <: Carrier
    type ∅ <: Carrier
    // the parser fails if I don't put a space before :
    val ∅ : ∅
    
    // bounds
    type in[S <: Carrier] <: { 
        type    is[E]
        type isnot[E]
      }

    // predicates
    type notIn[E, X <: Carrier]         <: Predicate
    type ∈[E, X <: Carrier]             <: Predicate
    type ⊂[S <: Carrier, Q <: Carrier]  <: Predicate
    type ~[S <: Carrier, Q <: Carrier]  <: Predicate
    type <<[S <: Carrier, Q <: Carrier] <: Predicate

    // functions
    type \[S <: Carrier, Q <: Carrier]          <: Fn2[S,Q] with AnyFn.withCodomain[Carrier]
    type ∪[S <: Carrier, Q <: Carrier]          <: Fn2[S,Q] with AnyFn.withCodomain[Carrier]

    type FirstOf[X <: Carrier, Z] <: Fn2[X,Z]
    type Pop[S <: Carrier, E] <: Fn1[S] with AnyFn.withCodomain[(E,Carrier)]
    type Choose[S <: Carrier, P <: Carrier] <: Fn2[S,P] with AnyFn.constant[P]
    type Replace[S <: Carrier, Q <: Carrier] <: Fn2[S,Q] with AnyFn.constant[S]
    type Reorder[S <: Carrier, Q <: Carrier] <: Fn2[S,Q] with AnyFn.constant[Q]

    import shapeless.{ HList, Poly }

    type SetMapper[F <: Poly, S <: Carrier] <: Fn2[F,S] with AnyFn.withCodomain[Carrier]

    // TODO review this one
    type SetMapFolder[S <: Carrier, F <: Poly, R] <: Fn3[S,F,R] with AnyFn.constant[R]

    type HListMapper[S <: Carrier, F <: Poly] <: Fn2[S,F] with AnyFn.withCodomain[HList]
    type ListMapper[S <: Carrier, F <: Poly] <: Fn2[S,F] { type Out <: List[_] }

    type ToHList[S <: Carrier] <: Fn1[S] with AnyFn.withCodomain[HList]
    type ToList[S <: Carrier] <: Fn1[S] { type Out <: List[_] }
    type ToListOf[S <: Carrier, O0] = ToList[S] { type Out <: List[O0] } 
  }

  trait Syntax {

    type MyTypes <: Types
    val types: MyTypes
    import types._
    
    type S <: types.Carrier
    val set: S

    def :~:[E](e: E)(implicit n: types.notIn[E,S]): types.:~:[E,S]

    def lookup[U](implicit 
      ev: notIn[U,S],
      firstOf: FirstOf[S,U]
    ): firstOf.Out

    // def pop[E](implicit e: E ∈ S, p: Pop[S, E]): p.Out

    // def project[P <: Carrier](implicit e: P ⊂ S, p: Choose[S, P]): p.Out

    // def replace[P <: Carrier](p: P)(implicit e: P ⊂ S, r: Replace[S, P]): r.Out

    // def reorder[P <: Carrier](implicit e: S ~ P, t: Reorder[S, P]): t.Out
    // def ~>[P <: Carrier](p: P)(implicit e: S ~ P, t: Reorder[S, P]): t.Out

    // def \[Q <: Carrier](q: Q)(implicit sub: S \ Q): sub.Out
    // def ∪[Q <: Carrier](q: Q)(implicit uni: S ∪ Q): uni.Out

    // import shapeless.Poly

    // def map[F <: Poly](f: F)(implicit mapper: SetMapper[S, F]): mapper.Out

    // def mapHList[F <: Poly](f: F)(implicit mapper: HListMapper[S, F]): mapper.Out
    // def mapList[F <: Poly](f: F)(implicit mapper: ListMapper[S, F]): mapper.Out

    // def mapFold[F <: Poly, R](f: F)(r: R)(op: (R, R) => R)(implicit mapFolder: SetMapFolder[S,F,R]): mapFolder.Out

    // def toHList(implicit toHList: ToHList[S]): toHList.Out
    // def toList(implicit toList: ToList[S]): toList.Out
    // def toListOf[O](implicit toListOf: (S ToListOf O)): toListOf.Out
  }