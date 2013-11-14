package ohnosequences.typesets.items

import ohnosequences.typesets._

object items {

  import syntax._

  implicit def itemOps[S <: TypeSet](s: S): ItemOps[S] = new ItemOps(s)
}

package ops {

  import ohnosequences.typesets.items._
  import shapeless.record._


  @annotation.implicitNotFound(msg = "No field ${K} in record ${S}")
  trait Selector[S <: TypeSet, K] {
    type Out
    def apply(l: S): Out
  }

  trait LowPrioritySelector {
    type Aux[S <: TypeSet, K, Out0] = Selector[S, K] { type Out = Out0 }

    implicit def setSelect[H, T <: TypeSet, K]
      (implicit st : Selector[T, K]): Aux[H :~: T, K, st.Out] =
        new Selector[H :~: T, K] {
          type Out = st.Out
          def apply(l : H :~: T): Out = st(l.tail)
        }
  }

  object Selector extends LowPrioritySelector {
    def apply[S <: TypeSet, K](implicit selector: Selector[S, K]): Aux[S, K, selector.Out] = selector

    implicit def setSelect1[K, V, T <: TypeSet]: Aux[FieldType[K, V] :~: T, K, V] =
      new Selector[FieldType[K, V] :~: T, K] {
        type Out = V
        def apply(l : FieldType[K, V] :~: T): Out = l.head
      }
  }
}

package syntax {

  final class ItemOps[S <: TypeSet](l: S) {
    import items._
    import ops._
    import shapeless._

    /**
     * Returns the value associated with the singleton typed key k. Only available if this record has a field with
     * with keyType equal to the singleton type k.T.
     */
    def get(k: Witness)(implicit selector : Selector[S, k.T]): selector.Out = selector(l)
  }

}

