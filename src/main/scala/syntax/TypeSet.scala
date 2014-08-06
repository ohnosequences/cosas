package ohnosequences.typesets.syntax

trait AnyTypeSetSyntax {

  // import AnyTypeSetSyntax._

  type MyTypes <: AnyTypeSetSyntax.Types
  val types: MyTypes
  import types._
  
  type S <: Carrier
  val set: S

  def :~:[E](e: E)(implicit n: E ∉ S): ohnosequences.typesets.:~:[E,S]

  def lookup[E](implicit ev: E ∈ S, l: (S FirstOf E)): E

  def pop[E](implicit e: E ∈ S, p: Pop[S, E]): p.Out

  def project[P <: Carrier](implicit e: P ⊂ S, p: Choose[S, P]): P

  def replace[P <: Carrier](p: P)(implicit e: P ⊂ S, r: Replace[S, P]): S

  def reorder[P <: Carrier](implicit e: S ~ P, t: Reorder[S, P]): P
  def ~>[P <: Carrier](p: P)(implicit e: S ~ P, t: Reorder[S, P]): P

  def \[Q <: Carrier](q: Q)(implicit sub: S \ Q): sub.Out
  def ∪[Q <: Carrier](q: Q)(implicit uni: S ∪ Q): uni.Out

  import shapeless._
  import poly._

  def map(f: Poly)(implicit m: SetMapper[f.type, S]): m.Out
  def mapHList(f: Poly)(implicit m: HListMapper[f.type, S]): m.Out
  def mapList(f: Poly)(implicit m: ListMapper[f.type, S]): m.Out

  def mapFold[R, F](z: R)(f: F)(op: (R, R) => R)
    (implicit smf: SetMapFolder[S, R, F]): R

  def toHList(implicit toHList: ToHList[S]): toHList.Out
  def toList(implicit toList: ToList[S]): toList.Out
  def toListWith[O](implicit toList: ToList.Aux[S, O]): toList.Out
}

object AnyTypeSetSyntax {

  import ohnosequences.typesets._

  type For[Set <: TypeSet] = AnyTypeSetSyntax { type S = Set }

  trait Types {

    // the carrier
    type Carrier

    

    // predicates
    type ∉[E, X <: Carrier] <: Predicate
    type ∈[E, X <: Carrier] <: Predicate
    type ⊂[S <: Carrier, Q <: Carrier] <: Predicate
    type ~[S <: Carrier, Q <: Carrier] <: Predicate
    type <<[S <: Carrier, Q <: Carrier] <: Predicate

    // functions
    type \[S <: Carrier, Q <: Carrier] <: Fn2[S,Q] with AnyFn.withCodomain[Carrier]
    type ∪[S,Q] <: Fn2[S,Q] with AnyFn.withCodomain[Carrier]

    type FirstOf[X <: Carrier, E] <: Fn2[X,E] with AnyFn.constant[E]
    type Pop[S <: Carrier, E] <: Fn1[S] with AnyFn.withCodomain[(E,Carrier)]
    type Choose[S <: Carrier, P <: Carrier] <: Fn2[S,P] with AnyFn.constant[P]
    type Replace[S <: Carrier, Q <: Carrier] <: Fn2[S,Q] with AnyFn.constant[S]
    type Reorder[S <: Carrier, Q <: Carrier] <: Fn2[S,Q] with AnyFn.constant[Q]
  }
  
}