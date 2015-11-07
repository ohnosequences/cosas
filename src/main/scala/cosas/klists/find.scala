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

class FindS[A] extends DepFn1[AnyKList { type Bound >: A }, A]

case object FindS extends FindSInTail {

  implicit def foundInHead[E <: T#Bound, H <: E, T <: AnyKList]
  : AnyApp1At[FindS[E], H :: T] { type Y = H } =
    App1 { (s: H :: T) => s.head }
}

trait FindSInTail {

  implicit def foundInTail[
    X, E >: X, H <: T#Bound,
    T  <: AnyKList { type Bound >: E }
  ](implicit
      l: AnyApp1At[FindS[E], T] { type Y = X }
  )
  : AnyApp1At[FindS[E], H :: T] { type Y = X } =
    App1 { (s: H :: T) =>  l(s.tail) }
}
