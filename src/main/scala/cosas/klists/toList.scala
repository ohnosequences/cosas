package ohnosequences.cosas.klists

import ohnosequences.cosas.fns._

class ToList[L <: AnyKList] extends DepFn1[L, List[L#Bound]]

case object ToList {

  implicit def empty[A]
  : App1[ToList[KNil[A]], KNil[A], List[A]] =
    App1 { x: KNil[A] => Nil: List[A] }

  implicit def nonempty[
    H <: A, T <: AnyKList { type Bound = A }, A](implicit
    toList: App1[ToList[T], T, List[A]]
  )
  : App1[ToList[H :: T], H :: T, List[A]] =
    App1 { xs: H :: T => xs.head :: toList(xs.tail) }
}

class ToListOf[L <: AnyKList.Of[B], B] extends DepFn1[L, List[B]]

case object ToListOf {

  implicit def empty[A, B >: A]
  : App1[ToListOf[KNil[A], B], KNil[A], List[B]] =
    App1 { x: KNil[A] => Nil: List[B] }

  implicit def nonempty[
    H <: T#Bound, T <: AnyKList { type Bound >: H <: B },
    B
  ](implicit
    toListOf: App1[ToListOf[T, B], T, List[B]]
  )
  : App1[ToListOf[H :: T, B], H :: T, List[B]] =
    App1 { xs: H :: T => xs.head :: toListOf(xs.tail) }
}
