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

  def ::[H0 <: Bound @uv](h: H0): KCons[H0,KNilOf[Bound @uv],Bound]  = KCons[H0,KNilOf[A],A](h,this)
}


trait NEKList[+A] extends KList[A] { neklist =>

  type Head <: Bound
  val  head: Head

  type Tail <: AnyKList //{ type Bound >: neklist.Bound @uv <: neklist.Bound @uv }
  val  tail: Tail

  // type Bound = Tail#Bound

  type Types = Tail#Types#or[Head]
  type Union = Types#union
}



// TODO could we get B from T as T#Bound?
case class KCons[+H <: BBB, +T <: KList[BBB], +BBB](val head: H, val tail: T) extends NEKList[BBB] with KList[BBB] {

  type Head = H @uv
  type Tail = T @uv

  def ::[H0 <: Bound @uv](h: H0): KCons[H0,KCons[H,T,Bound],Bound]  = KCons[H0,KCons[H,T,Bound],Bound](h,this)
}

case object AnyKList {

  type withBound[B] = AnyKList { type Bound = B }

  type is[L0 <: AnyKList] = L0 with AnyKList { type Bound = L0#Bound }

  type KNil[A] = KNilOf[A]
  def  KNil[A]: KNil[A] = new KNilOf[A]()

  implicit def knilOpsAlt[A](nil: KNil[A]): KListOpsAlt[KNil[A],A] = KListOpsAlt(nil)
  implicit def kconsOpsAlt[H <: X, T <: KList[X], X](ht: KCons[H,T,X]): KListOpsAlt[KCons[H,T,X], X] = KListOpsAlt(ht)

  implicit def klistops[L <: AnyKList](l: L): KListOps[L] = KListOps[L](l)

  // implicit def klistopsAlt[T <: KList[B],B](l: T): KListOpsAlt[T, B] = KListOpsAlt[T,B](l)
}

case class KListOps[L <: AnyKList](val l: L) extends AnyVal {

  // def ::[E <: L#Bound](e: E): KCons[E,L] = KCons[E,L](e, l)

  def head[H <: L#Bound, T <: KList[L#Bound]](implicit c: IsKCons[L,H,T]): H = c.h(l)

  def tail[H <: L#Bound, T <: KList[L#Bound]](implicit c: IsKCons[L,H,T]): T = c.t(l)
}

case class KListOpsAlt[L <: KList[B], B](val l: L) extends AnyVal {

  def map[F <: AnyDepFn1, O <: KList[F#Out]](f: F)(implicit
    mapper: MapKList[F,B,F#Out,L] { type OutK = O }
  )
  : O  = mapper(l)
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
  ]: IsKCons[KCons[H0,T0,B0], H0, T0] = new IsKCons[KCons[H0,T0,B0], H0, T0] {

    def h(l: KCons[H0,T0,B0]): H0 = l.head
    def t(l: KCons[H0,T0,B0]): T0 = l.tail
  }
}
