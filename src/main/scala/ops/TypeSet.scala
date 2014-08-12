package ohnosequences.typesets.ops

object defaultTypeSet {

  object Types extends ohnosequences.typesets.syntax.Types {

    import ohnosequences.typesets.{ AnyFn, Fn1, Fn2, Fn3, Predicate }

    import shapeless.{ HList, Poly }

    // the ADT
    type Carrier                                  = ohnosequences.typesets.TypeSet
    type :~:[E, X <: Carrier]                     = ohnosequences.typesets.:~:[E,X]
    type ∅                                        = ohnosequences.typesets.∅
    val ∅ : ∅                                     = ohnosequences.typesets.∅

    type in[S <: Carrier]                         = ohnosequences.typesets.in[S]

    type notIn[E, X <: Carrier]                   = ohnosequences.typesets.∉[E, X] 
    type ∈[E, X <: Carrier]                       = ohnosequences.typesets.∈[E, X]
    type ∪[S <: Carrier, Q <: Carrier]            = ohnosequences.typesets.Union[S,Q]
    type \[S <: Carrier, Q <: Carrier]            = ohnosequences.typesets.\[S,Q]
    type FirstOf[X <: Carrier, Z]                 = ohnosequences.typesets.Lookup[X,Z]
    type Pop[S <: Carrier, E]                     = ohnosequences.typesets.Pop[S,E]
    type Choose[S <: Carrier, P <: Carrier]       = ohnosequences.typesets.Choose[S,P]
    type Replace[S <: Carrier, Q <: Carrier]      = ohnosequences.typesets.Replace[S,Q]
    type Reorder[S <: Carrier, Q <: Carrier]      = ohnosequences.typesets.Reorder[S,Q]

    type SetMapper[F <: Poly, S <: Carrier]       = ohnosequences.typesets.SetMapper[F, S] 
    // TODO review this one
    type SetMapFolder[S <: Carrier, F <: Poly, R] = ohnosequences.typesets.SetMapFolder[S,F,R]

    type HListMapper[S <: Carrier, F <: Poly]     = ohnosequences.typesets.HListMapper[S,F]
    type ListMapper[S <: Carrier, F <: Poly]      = ohnosequences.typesets.ListMapper[S,F]

    type ToHList[S <: Carrier]                    = ohnosequences.typesets.ToHList[S]
    type ToList[S <: Carrier]            = ohnosequences.typesets.ToList[S]

    implicit def defaultOps[TS <: Carrier](set: TS): TypeSetOps[TS] = TypeSetOps(set)
  }

  case class TypeSetOps[TS <: Types.Carrier](val set: TS) 
  extends ohnosequences.typesets.syntax.Syntax { me =>

    type S = TS
    type MyTypes = Types.type
    val types: Types.type = Types
    import Types._

    // for convenience
    type TypeSet = types.Carrier

    def :~:[E](e: E)(implicit n: notIn[E,S]) = ohnosequences.typesets.:~:.cons(e, set)

    override def lookup[E](implicit ev: notIn[E,S], firstOf: types.FirstOf[S,E]): firstOf.Out = firstOf(set)

    def pop[E](implicit e: types.∈[E,S], p: Pop[S, E]): p.Out = p(set)

    def project[P <: TypeSet](implicit e: ⊂[P,S], p: types.Choose[S, P]): P = p(set)

    def replace[P <: TypeSet](p: P)(implicit e: P ⊂ S, r: Replace[S, P]): S = r(set, p)

    def reorder[P <: TypeSet](implicit e: S ~ P, t: Reorder[S, P]): P = t(set)
    def ~>[P <: TypeSet](p: P)(implicit e: S ~ P, t: Reorder[S, P]): P = t(set)

    def \[Q <: TypeSet](q: Q)(implicit sub: S \ Q): sub.Out = sub(set, q)
    def ∪[Q <: TypeSet](q: Q)(implicit uni: S ∪ Q): uni.Out = uni(set, q)

    import shapeless._
    import poly._
    def map(f: Poly)(implicit mapper: SetMapper[f.type, S]): mapper.Out = mapper(set)
    def mapHList(f: Poly)(implicit mapper: HListMapper[S, f.type]): mapper.Out = mapper(set)
    def mapList[F <: Poly](f: F)(implicit mapper: ListMapper[S, F]): mapper.Out = mapper(set)

    def mapFold[F <: Poly, R](f: F)(r: R)(op: (R, R) => R)(implicit smf: SetMapFolder[S,F,R]): R = smf(set, r, op)
    def toHList(implicit toHList: ToHList[S]): toHList.Out = toHList(set)
    def toList(implicit toList: ToList[S]): toList.Out = toList(set)
    def toListWith[O](implicit toList: ToListOf[S, O]): toList.Out = toList(set)
  }
}