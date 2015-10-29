/*
## Replace elements in one set with elements from another

The idea is that if `Q ⊂ S`, then you can replace some elements of `S`,
by the elements of `Q` with corresponding types. For example
`(1 :~: 'a' :~: "foo" :~: ∅) replace ("bar" :~: 2 :~: ∅) == (2 :~: 'a' :~: "bar" :~: ∅)`.
Note that the type of the result is the same (`S`).

*/

package ohnosequences.cosas.typeSets

import ohnosequences.cosas._, fns._

class Replace[S <: AnyTypeSet] extends DepFn2[S, AnyTypeSet, S]

case object Replace extends replace_2 {

  implicit def empty[S <: AnyTypeSet]: App2[Replace[S], S, ∅[S#Bound], S] =
    App2 { (s: S, q: ∅[S#Bound]) => s }

  implicit def replaceHead[
    H <: T#Bound, T <: AnyTypeSet,
    Q <: AnyTypeSet, QOut <: AnyTypeSet
  ](implicit
    popHead: App1[pop[H], Q, (H,QOut)],
    replace: App2[Replace[T], T, QOut, T]
  )
  : App2[Replace[H :~: T], H :~: T, Q, H :~: T] =
    App2 { (s: H :~: T, q: Q) => { val (h, qq) = popHead(q); h :~: replace(s.tail, qq) } }
}

trait replace_2 {

  implicit def skipHead[
    H <: T#Bound, T <: AnyTypeSet,
    Q <: AnyTypeSet, QOut <: AnyTypeSet
  ](implicit
    replace: App2[Replace[T], T,Q,T]
  )
  : App2[Replace[H :~: T], H :~: T, Q, H :~: T] =
    App2 { (s: H :~: T, q: Q) => s.head :~: replace(s.tail, q) }
}
