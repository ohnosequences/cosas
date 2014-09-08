package ohnosequences.pointless

trait AnyHList {

  type Types <: AnyTypeUnion
  type Union // should be Types#union, but we can't set it here
}

trait EmptyHList extends AnyHList {

  type Types = either[Nothing]
  type Union = Types#union
}

trait AnyKList {

  type Types <: AnyTypeUnion
  type Union // should be Types#union, but we can't set it here

  type Bound
}
object AnyKList {

  type BoundOf[K <: AnyKList] = K#Bound
  type withBound[B] = AnyKList { type Bound = B }
}

import AnyKList._

trait KList[A] extends AnyKList {

  type Bound = A
}

case class HNilOf[A]() extends KList[A] { listA =>

  type Types = either[Nothing]
  type Union = Types#union

  case class KCons[H <: listA.Bound, T <: KList[listA.Bound]](val head: H, val tail: T) extends NEKList[listA.Bound] {

    type Head = H
    type Tail = T
  }

  object KCons {

    implicit def kOps[L <: KList[listA.Bound]](l: L): KListOps[L] = KListOps(l)
  }

  implicit def klistOps[L <: KList[listA.Bound]](l: L): KListOps[L] = KListOps(l)

  case class KListOps[L <: KList[listA.Bound]](val l: L) {

    def :@@:[E <: listA.Bound](e: E): KCons[E,L] = KCons[E,L](e, l)

    // def nestedHead(implicit c: IsKCons[L,B]): c.H  = c.h(l)
  }
}

object HNilOf {

  implicit def emptyOps[A](nil: HNilOf[A]): nil.KListOps[HNilOf[A]] = nil.KListOps(nil)
}

trait AnyNEKList extends AnyKList { 

  type Head <: Bound
  val  head: Head

  type Tail <: AnyKList.withBound[Bound]
  val  tail: Tail
}

trait NEKList[A] extends AnyNEKList with KList[A] {}

case class KCons[H <: B, T <: KList[B], B](val head: H, val tail: T) extends NEKList[B] {

  type Head = H 
  type Tail = T

  type Types = Tail#Types#or[Head]
  type Union = Types#union
}

object AnyHList {

  def HNil[A]: HNilOf[A] = new HNilOf[A]()

  type HNil[A] = HNilOf[A]

  implicit def hnilOps[A](nil: HNil[A]): KListOps[HNil[A],A] = KListOps(nil)
  implicit def klistops[H <: B, T <: KList[B],B](l: KCons[H,T,B]): KListOps[KCons[H,T,B],B] = KListOps(l)

  case class KListOps[L <: KList[B],B](val l: L) {

    def :@:[E <: B](e: E): KCons[E,L,B] = KCons[E,L,B](e, l)

    def HEAD(implicit c: IsKCons[L,B]): c.H  = c.h(l)
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
    ]
    : IsKCons[KCons[H0,T0,B0], B0] = new IsKCons[KCons[H0,T0,B0], B0] {
        
        type T = T0
        type H = H0
        
        def h(l: KCons[H0,T0,B0]): H0 = l.head
        def t(l: KCons[H0,T0,B0]): T0 = l.tail
      }
  }

  // trait BoundedBy[L <: AnyKList, B <: BoundOf[L]] { 

  //   type Out = B
  // }

  // object BoundedBy {

  //   implicit def hnilBound[A]: BoundedBy[HNil[A],A] = new BoundedBy[HNil[A],A] {}
  //   implicit def kconsBound[H <: B0, T <: KList[B0], B0](implicit tailBound: BoundedBy[T,B0]): BoundedBy[KCons[H,T,B0], B0] = new BoundedBy[KCons[H,T,B0], B0] {}
  // }

  
}