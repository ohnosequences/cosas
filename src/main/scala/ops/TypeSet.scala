package ohnosequences.typesets.ops

import ohnosequences.typesets.syntax._

object DefaultTypes extends AnyTypeSetSyntax.Types {

  type Carrier = ohnosequences.typesets.TypeSet

  type ∪[S,Q] = ohnosequences.typesets.Union[S,Q]
  type FirstOf[S <: Carrier, E] = ohnosequences.typesets.Lookup[S,E]
  type Pop[S <: Carrier, E] = ohnosequences.typesets.Pop[S,E]
  type Choose[S <: Carrier, P <: Carrier] = ohnosequences.typesets.Choose[S,P]
}

case class TypeSetOps[TS <: ohnosequences.typesets.TypeSet](val set: TS) extends AnyTypeSetSyntax {

  type S = TS
  type MyTypes = DefaultTypes.type
  val types = DefaultTypes
  import types._

  // for convenience
  type TypeSet = Carrier

  def :~:[E](e: E)(implicit n: E ∉ S) = ohnosequences.typesets.:~:.cons(e, set)

  def lookup[E](implicit ev: E ∈ S, l: (S FirstOf E)): l.Out= l(set)

  def pop[E](implicit e: E ∈ S, p: types.Pop[S, E]): p.Out = p(set)

  def project[P <: TypeSet](implicit e: P ⊂ S, p: Choose[S, P]): P = p(set)

  def replace[P <: TypeSet](p: P)(implicit e: P ⊂ S, r: Replace[S, P]): S = r(set, p)

  def reorder[P <: TypeSet](implicit e: S ~ P, t: Reorder[S, P]): P = t(set)
  def ~>[P <: TypeSet](p: P)(implicit e: S ~ P, t: Reorder[S, P]): P = t(set)

  def \[Q <: TypeSet](q: Q)(implicit sub: S \ Q): sub.Out = sub(set, q)
  def ∪[Q <: TypeSet](q: Q)(implicit uni: S ∪ Q): uni.Out = uni(set, q)

  import shapeless._
  import poly._
  def map(f: Poly)(implicit m: SetMapper[f.type, S]): m.Out = m(set)
  def mapHList(f: Poly)(implicit m: HListMapper[f.type, S]): m.Out = m(set)
  def mapList(f: Poly)(implicit m: ListMapper[f.type, S]): m.Out = m(set)

  def mapFold[R, F](z: R)(f: F)(op: (R, R) => R)
    (implicit smf: SetMapFolder[S, R, F]): R = smf(set, z, op)

  def toHList(implicit toHList: ToHList[S]): toHList.Out = toHList(set)
  def toList(implicit toList: ToList[S]): toList.Out = toList(set)
  def toListWith[O](implicit toList: ToList.Aux[S, O]): toList.Out = toList(set)
}