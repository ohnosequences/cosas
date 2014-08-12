package ohnosequences.typesets

/* Just a combination of ~ and Choose (reordering set) */
@annotation.implicitNotFound(msg = "Can't reorder ${In} to ${O}")
trait Reorder[In <: TypeSet, O <: TypeSet] extends Fn2[In,O] {  type Out = O; def apply(s: In): Out }

object Reorder {
  implicit def any[In <: TypeSet, Out <: TypeSet]
    (implicit eq: In ~ Out, project: Choose[In, Out]): 
        Reorder[In, Out] = 
    new Reorder[In, Out] { def apply(s: In): Out = project(s) }
}
