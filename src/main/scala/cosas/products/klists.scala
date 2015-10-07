package ohnosequences.cosas.products

import ohnosequences.cosas._, typeUnions._

trait AnyKList {

  type Types <: AnyTypeUnion
  type Union // should be Types#union, but we can't set it here

  type Bound
}

trait KList[+A] extends AnyKList {

  type Bound = A @scala.annotation.unchecked.uncheckedVariance
}

case class KNilOf[+A]() extends KList[A] {

  type Types = TypeUnion.empty
  type Union = Types#union
}


trait AnyNEKList extends AnyKList { neklist =>

  type Head <: Bound
  val  head: Head

  type Tail <: AnyKList //{ type Bound = neklist.Bound }
  val  tail: Tail

  // type Bound = Tail#Bound

  type Types = Tail#Types#or[Head]
  type Union = Types#union
}

// trait NEKList[+A] extends KList[A] with N

// TODO could we get B from T as T#Bound?
case class KCons[+H <: T#Bound, +T <: AnyKList](val head: H, val tail: T) extends AnyNEKList with KList[T#Bound] {

  type Head = H @scala.annotation.unchecked.uncheckedVariance
  type Tail = T @scala.annotation.unchecked.uncheckedVariance
}

case object AnyKList {

  type withBound[B] = AnyKList { type Bound = B }

  type KNil[A] = KNilOf[A]
  def  KNil[A]: KNil[A] = new KNilOf[A]()

  implicit def hnilOps[A](nil: KNil[A]): KListOps[KNil[A],A] = KListOps(nil)

  implicit def klistops[H <: B, T <: KList[B],B](l: KCons[H,T]): KListOps[KCons[H,T],B] = KListOps(l)

  implicit def klistops2[H <: T#Bound, T <: AnyKList](l: KCons[H,T]): KListOps[KCons[H,T],T#Bound] = KListOps(l)
}

case class KListOps[L <: KList[B],B](val l: L) extends AnyVal {

  def ::[E <: B](e: E): KCons[E,L] = KCons[E,L](e, l)

  def head[H <: B, T <: KList[B]](implicit c: IsKCons[L,H,T,B]): H = c.h(l)

  def tail[H <: B, T <: KList[B]](implicit c: IsKCons[L,H,T,B]): T = c.t(l)
}

// TODO should be a depfn
trait IsKCons[L <: KList[B], H <: B, T <: KList[B], B] {

  def h(l: L): H
  def t(l: L): T
}

case object IsKCons {

  implicit def klistIsKCons[
    H0 <: B0,
    T0 <: KList[B0],
    B0
  ]: IsKCons[KCons[H0,T0], H0, T0, B0] = new IsKCons[KCons[H0,T0], H0, T0, B0] {

    def h(l: KCons[H0,T0]): H0 = l.head
    def t(l: KCons[H0,T0]): T0 = l.tail
  }
}
