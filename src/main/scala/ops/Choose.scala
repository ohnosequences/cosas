package ohnosequences.typesets

@annotation.implicitNotFound(msg = "Cannot choose from ${In} subset of type ${Out}")
trait Choose[In <: TypeSet, Out <: TypeSet] { def apply(s: In): Out }

object Choose {
  implicit def empty[In <: TypeSet]: 
        Choose[In, ∅] = 
    new Choose[In, ∅] { def apply(s: In) = ∅ }

  implicit def cons[In <: TypeSet, In_ <: TypeSet, H, T <: TypeSet]
    (implicit 
      pop: Pop.Aux[In, H, In_, H],
      rest: Choose[In_, T]
    ):  Choose[In, H :~: T] =
    new Choose[In, H :~: T] { 
      def apply(s: In) = {
        // val (h, s_) = pop(s)
        // h :~: rest(s_)

        val tpl = pop(s)
        tpl._1 :~: rest(tpl._2)
      }
    }
}
