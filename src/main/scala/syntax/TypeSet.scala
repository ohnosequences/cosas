package ohnosequences.typesets.syntax

import ohnosequences.typesets._

trait AnyTypeSetSyntax {
  
  type S <: TypeSet
  val set: S

  type FirstOf[X <: TypeSet, E] <: Fn2[X,E] with Out[E]

  def :~:[E](e: E)(implicit n: E ∉ S): ohnosequences.typesets.:~:[E,S]

  def lookup[E](implicit ev: E ∈ S, l: (S FirstOf E)): l.Out

  def pop[E](implicit e: E ∈ S, p: Pop[S, E]): p.Out

  def project[P <: TypeSet](implicit e: P ⊂ S, p: Choose[S, P]): P

  def replace[P <: TypeSet](p: P)(implicit e: P ⊂ S, r: Replace[S, P]): S

  def reorder[P <: TypeSet](implicit e: S ~ P, t: Reorder[S, P]): P
  def ~>[P <: TypeSet](p: P)(implicit e: S ~ P, t: Reorder[S, P]): P

  def \[Q <: TypeSet](q: Q)(implicit sub: S \ Q): sub.Out
  def ∪[Q <: TypeSet](q: Q)(implicit uni: S ∪ Q): uni.Out

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

  type For[Set <: TypeSet] = AnyTypeSetSyntax { type S = Set }
}

trait Lookup[S <: TypeSet, E] {

  type Out <: E
}