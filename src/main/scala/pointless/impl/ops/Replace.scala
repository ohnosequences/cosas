/* 
## Replace elements in one set with elements from another

The idea is that if `Q ⊂ S`, then you can replace some elements of `S`, 
by the elements of `Q` with corresponding types. For example 
`(1 :~: 'a' :~: "foo" :~: ∅) replace ("bar" :~: 2 :~: ∅) == (2 :~: 'a' :~: "bar" :~: ∅)`. 
Note that the type of the result is the same (`S`).

*/

package ohnosequences.pointless.impl.ops

import ohnosequences.pointless._, AnyFn._, impl._, typeSet._

@annotation.implicitNotFound(msg = "Can't replace elements in ${S} with ${Q}")
trait Replace[S <: AnyTypeSet, Q <: AnyTypeSet] extends Fn2[S, Q] with Constant[S]

object Replace extends Replace_2 {

  def apply[S <: AnyTypeSet, Q <: AnyTypeSet]
    (implicit replace: Replace[S, Q]): Replace[S, Q] with out[replace.Out] = replace

  implicit def empty[S <: AnyTypeSet]:
        Replace[S, ∅] = 
    new Replace[S, ∅] { def apply(s: S, q: ∅) = s }

  implicit def replaceHead[H, T <: AnyTypeSet, Q <: AnyTypeSet, QOut <: AnyTypeSet]
    (implicit 
      pop: Pop.Aux[Q, H, QOut, H],
      rest: Replace[T, QOut]
    ):  Replace[H :~: T, Q] =
    new Replace[H :~: T, Q] {
      def apply(s: H :~: T, q: Q): H :~: T = {
        val tpl = pop(q)
        tpl._1 :~: rest(s.tail, tpl._2)
      }
    }
}

trait Replace_2 {
  implicit def skipHead[H, T <: AnyTypeSet, Q <: AnyTypeSet, QOut <: AnyTypeSet]
    (implicit rest: Replace[T, Q]):
        Replace[H :~: T, Q] =
    new Replace[H :~: T, Q] {
      def apply(s: H :~: T, q: Q) = s.head :~: rest(s.tail, q)
    }
}
