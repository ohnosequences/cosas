package ohnosequences.cosas.klists

import ohnosequences.cosas._, fns._

class FindIn[A <: L#Bound, L <: AnyKList] extends DepFn1[L,A]

case object FindIn extends FoundInTail {

  implicit def foundInHead[A, T <: AnyKList { type Bound >: A }]
  : AnyApp1At[A FindIn (A :: T), A :: T] { type Y = A } =
    App1 { s: A :: T => s.head }
}

trait FoundInTail {

  implicit def foundInTail[
    H <: T#Bound,
    T <: AnyKList { type Bound >: A },
    A
  ](implicit
    findIn: AnyApp1At[A FindIn T, T] { type Y = A }
  )
  : AnyApp1At[A FindIn (H :: T), H :: T] { type Y = A } =
    App1 { s: H :: T => findIn(s.tail): A }
}
