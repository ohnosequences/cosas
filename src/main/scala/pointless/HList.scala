package ohnosequences.pointless

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


trait AnyNEKList extends AnyKList {

  type Head <: Bound
  val  head: Head

  import AnyKList._
  type Tail <: AnyKList.withBound[Bound]
  val  tail: Tail

  type Types = Tail#Types#or[Head]
  type Union = Types#union
}

trait NEKList[+A] extends AnyNEKList with KList[A]


case class KCons[+H <: B, +T <: KList[B], +B](val head: H, val tail: T) extends NEKList[B] {

  type Head = H @scala.annotation.unchecked.uncheckedVariance
  type Tail = T @scala.annotation.unchecked.uncheckedVariance
}


object AnyKList {

  type withBound[B] = AnyKList { type Bound = B }

  type KNil[A] = KNilOf[A]
  def  KNil[A]: KNil[A] = new KNilOf[A]()

  implicit def hnilOps[A](nil: KNil[A]): KListOps[KNil[A],A] = KListOps(nil)

  implicit def klistops[H <: B, T <: KList[B],B](l: KCons[H,T,B]): KListOps[KCons[H,T,B],B] = KListOps(l)
}

case class KListOps[L <: KList[B],B](val l: L) {

  def :@:[E <: B](e: E): KCons[E,L,B] = KCons[E,L,B](e, l)

  def HEAD(implicit c: IsKCons[L,B]): c.H = c.h(l)
  def TAIL(implicit c: IsKCons[L,B]): c.T = c.t(l)

}

trait IsKCons[L <: KList[B],B] {

  type H <: B
  type T <: KList[B]

  def h(l: L): H
  def t(l: L): T
}

object IsKCons {

  implicit def klistIsKCons[
    H0 <: B0,
    T0 <: KList[B0],
    B0
  ]: IsKCons[KCons[H0,T0,B0], B0] = new IsKCons[KCons[H0,T0,B0], B0] {

    type T = T0
    type H = H0

    def h(l: KCons[H0,T0,B0]): H0 = l.head
    def t(l: KCons[H0,T0,B0]): T0 = l.tail
  }
}
