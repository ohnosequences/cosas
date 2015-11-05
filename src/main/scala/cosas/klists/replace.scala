package ohnosequences.cosas.klists

import ohnosequences.cosas._, fns._

class Replace[S <: AnyKList] extends DepFn2[S, AnyKList, S]

case object Replace extends ReplaceInTail {

  implicit def empty[S <: AnyKList { type Bound = X }, X]: App2[Replace[S], S, *[X], S] =
    App2 { (s: S, q: *[S#Bound]) => s }

  implicit def replaceHead[
    H <: T#Bound, T <: AnyKList,
    Q <: AnyKList, QOut <: AnyKList
  ](implicit
    pickHead: App1[pick[H], Q, (H,QOut)],
    replace: AnyApp2At[Replace[T], T, QOut] { type Y = T }
  )
  : App2[Replace[H :: T], H :: T, Q, H :: T] =
    App2 { (s: H :: T, q: Q) => { val (h, qq) = pickHead(q); h :: replace(s.tail, qq) } }
}

trait ReplaceInTail {

  implicit def skipHead[
    H <: T#Bound, T <: AnyKList,
    Q <: AnyKList, QOut <: AnyKList
  ](implicit
    replace: AnyApp2At[Replace[T], T,Q] { type Y = T }
  )
  : App2[Replace[H :: T], H :: T, Q, H :: T] =
    App2 { (s: H :: T, q: Q) => s.head :: replace(s.tail, q) }
}
