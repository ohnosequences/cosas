package ohnosequences.cosas.products

import ohnosequences.cosas._, typeUnions._, fns._

import scala.annotation.unchecked.{uncheckedVariance => uv}

trait AnyKList {

  type Types <: AnyTypeUnion
  type Union // should be Types#union, but we can't set it here

  type Bound
}

trait KList[+A] extends AnyKList {

  type Bound = A @uv
}

trait AnyEmptyKList extends AnyKList

case class KNilOf[+A]() extends AnyEmptyKList with KList[A] {

  type Types = TypeUnion.empty
  type Union = Types#union
}

trait NEKList[+A] extends KList[A] { neklist =>

  type Head <: Bound
  val  head: Head

  type Tail <: AnyKList //{ type Bound = neklist.Bound }
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

  type is[L0 <: AnyKList] = L0 with AnyKList { type Bound = L0#Bound }

  implicit def reallyIam[L <: AnyKList](l: L): AnyKList.is[L] = l.asInstanceOf[AnyKList.is[L]]
  implicit def klistSyntax[L <: AnyKList](l: L): KListSyntax[L] = KListSyntax[L](l)
}

case class KListSyntax[L <: AnyKList](val l: L) extends AnyVal {

  def ::[E <: L#Bound](e: E): KCons[E,L] = KCons[E,L](e, l)

  def head[H <: L#Bound, T <: KList[L#Bound]](implicit c: IsKCons[L,H,T]): H = c.h(l)

  def tail[H <: L#Bound, T <: KList[L#Bound]](implicit c: IsKCons[L,H,T]): T = c.t(l)
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
  ]: IsKCons[KCons[H0,T0], H0, T0] = new IsKCons[KCons[H0,T0], H0, T0] {

    def h(l: KCons[H0,T0]): H0 = l.head
    def t(l: KCons[H0,T0]): T0 = l.tail
  }
}
