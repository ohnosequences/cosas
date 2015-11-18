package ohnosequences.cosas.klists

import ohnosequences.cosas._, typeUnions._, fns._

sealed trait AnyKList extends Any {

  type Bound

  type Length <: AnyNat

  type Types <: AnyTypeUnion
  // NOTE: should be Types#union, but we can't set it here; scalac bugs
  type Union >: Types#union <: Types#union
}

case object KList {

  // NOTE need to be here to drive type inference, cannot be in syntax
  def apply[F <: AnyDepFn1](f: F): mapKList[F] = mapKList[F]
}

sealed trait AnyEmptyKList extends Any with AnyKList {

  type Types = TypeUnion.empty
  type Union = Types#union

  type Length = _0
}

case class KNilOf[+A]() extends AnyEmptyKList {

  type Bound = A @uv
}

sealed trait AnyNonEmptyKList extends Any with AnyKList {

  type Head <: Bound
  val  head: Head

  type Tail <: AnyKList
  val  tail: Tail

  type Bound >: Tail#Bound <: Tail#Bound // NOTE again this is for forcing type inference

  type Types = Tail#Types#or[Head]
  type Union = Types#union

  type Length = Successor[Tail#Length]
}

case object AnyNonEmptyKList {

  type Of[+B] = AnyNonEmptyKList { type Bound <: B }
  type withBound[B] = AnyNonEmptyKList { type Bound = B }
}

case class KCons[+H <: T#Bound, +T <: AnyKList](val head: H, val tail: T) extends AnyNonEmptyKList {

  type Bound = T#Bound @uv
  type Head = H @uv
  type Tail = T @uv
}

case object AnyKList {

  type Of[+B] = AnyKList { type Bound <: B }
  type withBound[B] = AnyKList { type Bound = B }

  implicit def klistSyntax[L <: AnyKList](l: L)
  : syntax.KListSyntax[L] =
    syntax.KListSyntax[L](l)
}

// TODO should be a depfn
trait IsKCons[L <: AnyKList, H <: L#Bound, T <: AnyKList] {

  def h(l: L): H
  def t(l: L): T
}

case object IsKCons {

  implicit def default[
    H0 <: T0#Bound,
    T0 <: AnyKList
  ]
  : IsKCons[KCons[H0,T0], H0, T0] =
    new IsKCons[KCons[H0,T0], H0, T0] {

    def h(l: KCons[H0,T0]): H0 = l.head
    def t(l: KCons[H0,T0]): T0 = l.tail
  }
}
