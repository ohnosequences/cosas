/* 
## Replace elements in one set with elements from another

The idea is that if `Q ⊂ S`, then you can replace some elements of `S`, 
by the elements of `Q` with corresponding types. For example 
`(1 :~: 'a' :~: "foo" :~: ∅) replace ("bar" :~: 2 :~: ∅) == (2 :~: 'a' :~: "bar" :~: ∅)`. 
Note that the type of the result is the same (`S`).

*/

package ohnosequences.pointless.ops.typeSet

import ohnosequences.pointless._, AnyFn._, AnyTypeSet._

@annotation.implicitNotFound(msg = "Can't replace elements in ${S} with ${Q}")
trait Replace[S <: AnyTypeSet, Q <: AnyTypeSet] extends Fn2[S, Q] with Out[S]

object Replace extends Replace_2 {

  def apply[S <: AnyTypeSet, Q <: AnyTypeSet]
    (implicit replace: Replace[S, Q]): Replace[S, Q] = replace

  implicit def empty[S <: AnyTypeSet, E <: AnyEmptySet]:
        Replace[S, E] = 
    new Replace[S, E] { def apply(s: In1, q: In2): Out = s }

  implicit def replaceHead[
    H <: T#Bound, 
    T <: AnyTypeSet, 
    Q <: AnyTypeSet,
    QOut <: AnyTypeSet
  ](implicit
      pop: Pop[Q, H] { type SOut = QOut },
      rest: Replace[T, QOut]
    ):  Replace[H :~: T, Q] =
    new Replace[H :~: T, Q] {

      def apply(s: In1, q: In2): Out = {
        val (h, qq) = pop(q)
        val t: T = rest(s.tail, qq)
        h :~: t
      }
    }
}

trait Replace_2 {
  implicit def skipHead[H <: T#Bound, T <: AnyTypeSet, Q <: AnyTypeSet]
    (implicit rest: Replace[T, Q]):
        Replace[H :~: T, Q] =
    new Replace[H :~: T, Q] {

      def apply(s: In1, q: In2): Out = s.head :~: rest(s.tail, q)
    }
}
