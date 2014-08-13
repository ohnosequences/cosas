package ohnosequences.typesets.impl



object defaultTypeSets extends ohnosequences.typesets.syntax.typesets {

  object defaultTypes extends types {
  
    import shapeless.{ HList, Poly }

    // the ADT
    type TypeSet                                  = ohnosequences.typesets.TypeSet
    type :~:[E, X <: TypeSet]                     = ohnosequences.typesets.:~:[E,X]
    type ∅                                        = ohnosequences.typesets.∅
    val ∅ : ∅                                     = ohnosequences.typesets.∅

    type in[S <: TypeSet]                         = ohnosequences.typesets.in[S]
    type subsetOf[S <: TypeSet]                   = ohnosequences.typesets.subsetOf[S]

    type notIn[E, X <: TypeSet]                   = ohnosequences.typesets.∉[E, X] 
    
    type ∪[S <: TypeSet, Q <: TypeSet]            = ohnosequences.typesets.Union[S,Q]
    type \[S <: TypeSet, Q <: TypeSet]            = ohnosequences.typesets.\[S,Q]
    type FirstOf[X <: TypeSet, Z]                 = ohnosequences.typesets.Lookup[X,Z]
    type Pop[S <: TypeSet, E]                     = ohnosequences.typesets.Pop[S,E]
    type Choose[S <: TypeSet, P <: TypeSet]       = ohnosequences.typesets.Choose[S,P]
    type Replace[S <: TypeSet, Q <: TypeSet]      = ohnosequences.typesets.Replace[S,Q]
    type Reorder[S <: TypeSet, Q <: TypeSet]      = ohnosequences.typesets.Reorder[S,Q]

    type SetMapper[F <: Poly, S <: TypeSet]       = ohnosequences.typesets.SetMapper[F, S] 
    // TODO review this one
    type SetMapFolder[S <: TypeSet, F <: Poly, R] = ohnosequences.typesets.SetMapFolder[S,F,R]

    type HListMapper[S <: TypeSet, F <: Poly]     = ohnosequences.typesets.HListMapper[S,F]
    type ListMapper[S <: TypeSet, F <: Poly]      = ohnosequences.typesets.ListMapper[S,F]

    type ToHList[S <: TypeSet]                    = ohnosequences.typesets.ToHList[S]
    type ToList[S <: TypeSet]                     = ohnosequences.typesets.ToList[S]
    // type ToListOf[S <: TypeSet, O]                = ohnosequences.typesets.ToList.Aux[S,O]

    implicit def defaultOps[TS <: TypeSet](set: TS): defaultOps[TS] = defaultOps(set)
  }

  

  case class defaultOps[S <: defaultTypes.TypeSet](val s: S) extends defaultTypes.ops[S](s) {

    import defaultTypes._

    def :~:[E](e: E)(implicit n: (E ∉ S)) = ohnosequences.typesets.:~:(e, set)

    def lookup[E](implicit ev: (E ∈ S), firstOf: FirstOf[S,E]): firstOf.Out = firstOf(set)

    def pop[E](implicit e: (E ∈ S), p: Pop[S, E]): p.Out = p(set)

    def project[P <: TypeSet](implicit e: ⊂[P,S], p: Choose[S, P]): P = p(set)

    def replace[P <: TypeSet](p: P)(implicit e: P ⊂ S, r: Replace[S, P]): S = r(set, p)

    def reorder[P <: TypeSet](implicit e: S ~ P, t: Reorder[S, P]): P = t(set)
    def ~>[P <: TypeSet](p: P)(implicit e: S ~ P, t: Reorder[S, P]): P = t(set)

    def \[Q <: TypeSet](q: Q)(implicit sub: S \ Q): sub.Out = sub(set, q)
    def ∪[Q <: TypeSet](q: Q)(implicit uni: S ∪ Q): uni.Out = uni(set, q)

    import shapeless.Poly
    def map[F <: Poly](f: F)(implicit mapper: SetMapper[F, S]): mapper.Out = mapper(set)
    def mapHList[F <: Poly](f: F)(implicit mapper: HListMapper[S, F]): mapper.Out = mapper(set)
    def mapList[F <: Poly](f: F)(implicit mapper: ListMapper[S, F]): mapper.Out = mapper(set)

    def mapFold[F <: Poly, R](f: F)(r: R)(op: (R, R) => R)(implicit smf: SetMapFolder[S,F,R]): R = smf(set, r, op)
    def toHList(implicit toHList: ToHList[S]): toHList.Out = toHList(set)
    def toList(implicit toList: ToList[S]): toList.Out = toList(set)
    def toListOf[O](implicit toList: ToListOf[S, O]): toList.Out = toList(set)
  }
}

