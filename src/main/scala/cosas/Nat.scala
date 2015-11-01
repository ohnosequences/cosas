package ohnosequences.cosas

import ohnosequences.cosas._, fns._, typeUnions._

trait AnyNat { n =>

  type Next <: AnyNat { type Pred = n.type }
  val next: Next

  type StrictlySmaller <: AnyTypeUnion
}

case object zero extends AnyNat {

  type Next = Successor[this.type]
  lazy val next: Next = Successor(zero)

  type StrictlySmaller = empty
}
trait AnyNonZeroNat extends AnyNat { nz =>

  type Next <: AnyNonZeroNat { type Pred = nz.type }
  type Pred <: AnyNat
  val pred: Pred

  type StrictlySmaller = Pred#StrictlySmaller or Pred
}

case class Successor[N <: AnyNat](val pred: N) extends AnyNonZeroNat {

  type Next = Successor[this.type]
  lazy val next: Next = Successor(this)
  type Pred = N
}

case object sum extends DepFn2[AnyNat, AnyNat, AnyNat] {

  implicit def zeroPlusAnything[N <: AnyNat]
  : AnyApp2At[sum.type, N, _0] { type Y = N } =
    App2 { (n: N, o: _0) => n }

  implicit def rec[X <: AnyNat, Y <: AnyNat, Z <: AnyNat](implicit
    sumN: AnyApp2At[sum.type, Successor[X], Y] { type Y = Z }
  )
  : AnyApp2At[sum.type, X, Successor[Y]] { type Y = Z }=
    App2 { (n: X, o: Successor[Y]) => sumN( Successor(n), o.pred ) }
}
