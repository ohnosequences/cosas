package ohnosequences.cosas.klists

import ohnosequences.cosas.fns._

// TODO remove this. Useless.
class toList[L <: AnyKList] extends DepFn1[L, List[L#Bound]]

case object toList {

  implicit def empty[A]
  : App1[toList[KNil[A]], KNil[A], List[A]] =
    App1 { x: KNil[A] => Nil: List[A] }

  implicit def nonempty[
    H <: A, T <: AnyKList { type Bound = A }, A](implicit
    toList: App1[toList[T], T, List[A]]
  )
  : App1[toList[H :: T], H :: T, List[A]] =
    App1 { xs: H :: T => xs.head :: toList(xs.tail) }
}

class toListOf[L <: AnyKList.Of[B], B] extends DepFn1[L, List[B]]

case object toListOf {

  implicit def empty[A, B >: A]
  : App1[toListOf[KNil[A], B], KNil[A], List[B]] =
    App1 { x: KNil[A] => Nil: List[B] }

  implicit def nonempty[
    H <: T#Bound, T <: AnyKList { type Bound >: H <: B },
    B
  ](implicit
    toListOf: App1[toListOf[T, B], T, List[B]]
  )
  : App1[toListOf[H :: T, B], H :: T, List[B]] =
    App1 { xs: H :: T => xs.head :: toListOf(xs.tail) }
}
