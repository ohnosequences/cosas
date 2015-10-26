package ohnosequences.cosas.klists

import ohnosequences.cosas._, typeUnions._, fns._

import scala.annotation.unchecked.{ uncheckedVariance => uv }

trait AnyKList extends Any {

  type Types <: AnyTypeUnion
  type Union // NOTE should be Types#union, but we can't set it here; scalac bugs

  type Bound
}

case object KList {

  def apply[F <: AnyDepFn1](f: F): mapKList[F] = mapKList[F](f)
}

trait KList[+A] extends Any with AnyKList {

  type Bound = A @uv
}

trait AnyEmptyKList extends Any with AnyKList

case class KNilOf[+A]private[klists](val unique: KNilOf.type) extends AnyVal with AnyEmptyKList with KList[A] {

  type Types = TypeUnion.empty
  type Union = Types#union
}

trait NEKList[+A] extends KList[A] { neklist =>

  type Head <: Bound
  val  head: Head

  type Tail <: AnyKList // { type Bound = neklist.Bound } NOTE again scalac
  val  tail: Tail

  type Types = Tail#Types#or[Head]
  type Union = Types#union
}

case class KCons[+H <: T#Bound, +T <: AnyKList](val head: H, val tail: T) extends NEKList[T#Bound] with KList[T#Bound] {

  type Head = H @uv
  type Tail = T @uv
}

case object AnyKList {

  type withBound[B] = AnyKList { type Bound = B }

  implicit def klistSyntax[L <: AnyKList](l: L)
  : KListSyntax[L] =
    KListSyntax[L](l)
}

case class KListSyntax[L <: AnyKList](val l: L) extends AnyVal {

  def head[
    H <: L#Bound,
    T <: KList[L#Bound]
  ](implicit
    c: IsKCons[L,H,T]
  )
  : H =
    c.h(l)

  def tail[
    H <: L#Bound,
    T <: KList[L#Bound]
  ](implicit
    c: IsKCons[L,H,T]
  )
  : T =
    c.t(l)

  def at[N <: AnyNat, Z <: L#Bound](n: Witness[N])(implicit a: App1[L at N, L, Z]): Z =
    a(l)

  def ::[E <: L#Bound](e: E)
  : KCons[E,L] =
    KCons[E,L](e, l)

  def find[A <: L#Bound](implicit
    findIn: App1[A findIn L, L, A]
  )
  : A =
    findIn(l)

  def toList(implicit
    conv: App1[toList[L], L, List[L#Bound]]
  )
  : List[L#Bound] =
    conv(l)

  def ++[
    S <: AnyKList { type Bound = L#Bound},
    LS <: AnyKList { type Bound = L#Bound }
  ](s: S)(implicit
    concat: App2[Concatenate[L], L, S, LS]
  )
  : LS =
    concat(l,s)

  def foldLeft[
    F <: AnyDepFn2,
    Z <: F#Out,
    O <: F#Out
  ](f: F)(z: Z)(implicit
    mmm: AnyApp3At[FoldLeft[L,F,Z],L,Z,F] { type Y = O }
  )
  : O =
    mmm(l,z,f)
}

// TODO should be a depfn
trait IsKCons[L <: AnyKList, H <: L#Bound, T <: KList[L#Bound]] {

  def h(l: L): H
  def t(l: L): T
}

case object IsKCons {

  implicit def klistIsKCons[
    H0 <: B0,
    T0 <: KList[B0],
    B0
  ]
  : IsKCons[KCons[H0,T0], H0, T0] =
    new IsKCons[KCons[H0,T0], H0, T0] {

    def h(l: KCons[H0,T0]): H0 = l.head
    def t(l: KCons[H0,T0]): T0 = l.tail
  }
}
