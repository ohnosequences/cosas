package ohnosequences.typesets

/* Just a combination of ~ and Choose (reordering set) */
@annotation.implicitNotFound(msg = "Can't reorder ${In} to ${Out}")
trait Reorder[In <: TypeSet, Out <: TypeSet] { def apply(s: In): Out }

object Reorder {
  implicit def any[In <: TypeSet, Out <: TypeSet]
    (implicit eq: In ~ Out, project: Choose[In, Out]): 
        Reorder[In, Out] = 
    new Reorder[In, Out] { def apply(s: In): Out = project(s) }
}
