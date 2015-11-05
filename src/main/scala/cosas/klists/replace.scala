package ohnosequences.cosas.klists

import ohnosequences.cosas._, fns._

/*
## Replace elements in one set with elements from another

The idea is that if `Q ⊂ S`, then you can replace some elements of `S`,
by the elements of `Q` with corresponding types. For example
`(1 :: 'a' :: "foo" :: *) replace ("bar" :: 2 :: *) == (2 :: 'a' :: "bar" :: *)`.
Note that the type of the result is the same (`S`).

*/

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
