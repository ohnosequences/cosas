package ohnosequences.cosas.unions

import ohnosequences.cosas._, typeUnions._

trait AnyUnion {

  type Bound

  type Values <: AnyTypeUnion
  type Size <: AnyNat
}

trait UnionOf[+B] extends AnyUnion { type Bound = B @uv }

class Empty[+B] extends UnionOf[B] {

  type Values = empty
  type Size = _0
}

trait AnyNonEmptyUnion extends AnyUnion { ne =>

  type Last <: Bound

  type Others <: AnyUnion

  type Bound = Others#Bound
  type Size = Successor[Others#Size]
  type Values = Others#Values or ValueAt[Last,Size]
}

case object AnyNonEmptyUnion {

  implicit def syntax[U <: AnyNonEmptyUnion](v: Witness[U]): AnyNonEmptyUnionSyntax[U] =
    new AnyNonEmptyUnionSyntax(v.unique)
}

class Or[+T <: AnyUnion, +H <: T#Bound] extends AnyNonEmptyUnion {

  type Last = H @uv
  type Others = T @uv
}

case class ValueAt[V, Pos <: AnyNat](val v: V) extends AnyVal

sealed trait AnyUnionValue extends Any {

  type Union <: AnyNonEmptyUnion

  type Value <: Union#Bound
  def value: Value

  type Position <: AnyNat

  // val validValue: ValueAt[Value,Position] isOneOf Union#Values
}

case class AnyNonEmptyUnionSyntax[U <: AnyNonEmptyUnion](val witness: Witness.type) extends AnyVal {

  def at[
    V <: U#Bound,
    Pos <: AnyNat
  ](pair: (Witness[Pos], V))(implicit
    validValue: ValueAt[V,Pos] isOneOf U#Values
  )
  : UnionValue[U,V,Pos] =
    UnionValue.build[U,V,Pos](pair._2)
}

sealed trait ValueOfUnion[+U <: AnyNonEmptyUnion] extends Any with AnyUnionValue { type Union = U @uv }

case object UnionValue {

  def build[
    U <: AnyNonEmptyUnion,
    V <: U#Bound,
    Pos <: AnyNat
  ](value: V)(implicit
    validValue: ValueAt[V,Pos] isOneOf U#Values
  ): UnionValue[U,V,Pos] = new UnionValue[U,V,Pos](value)
}
case class UnionValue[
  +U <: AnyNonEmptyUnion,
  V <: U#Bound,
  Pos <: AnyNat
] private (val value: V) extends ValueOfUnion[U] {

  type Value = V; type Position = Pos
}
