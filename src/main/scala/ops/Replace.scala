/* 
## Replace elements in one set with elements from another

The idea is that if `Q ⊂ S`, then you can replace some elements of `S`, 
by the elements of `Q` with corresponding types. For example 
`(1 :~: 'a' :~: "foo" :~: ∅) replace ("bar" :~: 2 :~: ∅) == (2 :~: 'a' :~: "bar" :~: ∅)`. 
Note that the type of the result is the same (`S`).

*/

package ohnosequences.typesets


@annotation.implicitNotFound(msg = "Can't replace elements in ${S} with ${Q}")
trait Replace[S <: TypeSet, Q <: TypeSet] {
  def apply(s: S, q: Q): S
}

object Replace extends Replace_2 {

  implicit def empty[S <: TypeSet]:
        Replace[S, ∅] = 
    new Replace[S, ∅] { def apply(s: S, q: ∅) = s }

  implicit def replaceHead[H, T <: TypeSet, Q <: TypeSet, QOut <: TypeSet]
    (implicit 
      pop: Pop.Aux[Q, H, QOut, H],
      rest: Replace[T, QOut]
    ):  Replace[H :~: T, Q] =
    new Replace[H :~: T, Q] {
      def apply(s: H :~: T, q: Q): H :~: T = {
        
        // val (h, qout) = pop(q)
        // h :~: rest(s.tail, qout)
        val tpl = pop(q)
        tpl._1 :~: rest(s.tail, tpl._2)
      }
    }
}

trait Replace_2 {
  implicit def skipHead[H, T <: TypeSet, Q <: TypeSet, QOut <: TypeSet]
    (implicit rest: Replace[T, Q]):
        Replace[H :~: T, Q] =
    new Replace[H :~: T, Q] {
      def apply(s: H :~: T, q: Q) = s.head :~: rest(s.tail, q)
    }
}
