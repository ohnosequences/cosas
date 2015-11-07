package ohnosequences.cosas.klists

import ohnosequences.cosas._, fns._

class FindIn[A <: L#Bound, L <: AnyKList] extends DepFn1[L,A]

case object FindIn extends FoundInTail {

  implicit def foundInHead[H <: A, T <: AnyKList, A <: T#Bound]
  : App1[A FindIn (H :: T), H :: T, A] =
    App1 { s: H :: T => s.head }
}

trait FoundInTail {

  implicit def foundInTail[
    H <: T#Bound,
    T <: AnyKList { type Bound >: A },
    A
  ](implicit
    findIn: App1[A FindIn T, T, A]
  )
  : App1[A FindIn (H :: T), H :: T, A] =
    App1 { s: H :: T => findIn(s.tail) }
}

class FindSubtypeOf[X <: A, A] extends DepFn1[AnyKList, X]

case object FindSubtypeOf extends FindSubtypeOfInTail {

  implicit def foundInHead[E <: T#Bound, H <: E, T <: AnyKList]
  : AnyApp1At[H FindSubtypeOf E, H :: T] { type Y = H } =
    App1 { (s: H :: T) => s.head }
}

trait FindSubtypeOfInTail {

  implicit def foundInTail[
    X, E >: X, H,
    T  <: AnyKList { type Bound >: H }
  ](implicit
      l: AnyApp1At[FindSubtypeOf[X,E], T] { type Y = X }
  )
  : AnyApp1At[FindSubtypeOf[X,E], H :: T] { type Y = X } =
    App1 { (s: H :: T) =>  l(s.tail) }
}
