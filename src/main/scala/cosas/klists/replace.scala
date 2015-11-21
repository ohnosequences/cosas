package ohnosequences.cosas.klists

import ohnosequences.cosas._, fns._

class replace[S <: AnyKList] extends DepFn2[S, AnyKList, S]

case object replace extends replaceInTail {

  implicit def empty[S <: AnyKList { type Bound = X }, X]:
    AnyApp2At[replace[S], S, *[X]] { type Y = S } =
    App2 { (s: S, q: *[S#Bound]) => s }

  implicit def replaceHead[
    H <: T#Bound, T <: AnyKList,
    Q <: AnyKList, QOut <: AnyKList
  ](implicit
    pickHead: AnyApp1At[pick[H], Q] { type Y = (H,QOut) },
    replace: AnyApp2At[replace[T], T, QOut] { type Y = T }
  )
  : AnyApp2At[replace[H :: T], H :: T, Q] { type Y = H :: T } =
    App2 { (s: H :: T, q: Q) => { val (h, qq) = pickHead(q); h :: replace(s.tail, qq) } }
}

trait replaceInTail {

  implicit def skipHead[
    H <: T#Bound, T <: AnyKList,
    Q <: AnyKList, QOut <: AnyKList
  ](implicit
    replace: AnyApp2At[replace[T], T,Q] { type Y = T }
  )
  : AnyApp2At[replace[H :: T], H :: T, Q] { type Y = H :: T } =
    App2 { (s: H :: T, q: Q) => s.head :: replace(s.tail, q) }
}
