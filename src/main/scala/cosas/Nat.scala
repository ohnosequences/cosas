package ohnosequences.cosas

import ohnosequences.cosas.fns._

trait AnyNat { n =>

  type Next <: AnyNat { type Pred = n.type }
}
case object AnyNat {
}

case object zero extends AnyNat {

  type Next = Successor[this.type]
}
trait AnyNonZeroNat extends AnyNat { nz =>

  type Next <: AnyNonZeroNat { type Pred = nz.type }
  type Pred <: AnyNat
}

trait Successor[N <: AnyNat] extends AnyNonZeroNat {

  type Next = Successor[this.type]
  type Pred = N
}

case object sum extends DepFn2[Witness.Of[AnyNat], Witness.Of[AnyNat], Witness.Of[AnyNat]] {

  implicit def zeroPlusAnything[N <: AnyNat]
  : AnyApp2At[sum.type, Witness[N], Witness[_0]] { type Y = Witness[N] } =
    App2 { (n: Witness[N], o: Witness[_0]) => n }

  implicit def rec[
    X <: AnyNat,
    Y <: AnyNat,
    Z <: AnyNat
  ](implicit
    sumN: AnyApp2At[sum.type, Witness[Successor[X]], Witness[Y]] { type Y = Witness[Z] }
  )
  : AnyApp2At[sum.type, Witness[X], Witness[Successor[Y]]] { type Y = Witness[Z] }=
    App2 { (n: Witness[X], o: Witness[Successor[Y]]) => witness[Z] }
}
