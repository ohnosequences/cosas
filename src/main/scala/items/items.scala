package ohnosequences.typesets.items

import ohnosequences.typesets._

// the idea is that
// 
//    1. Syntax needs Ops
//    2. for Ops you need a Carrier
//    3. for providing Ops, you need Syntax
//    4. and the carrier bounds a particular GADT "shape"
// 
object experiment {

  trait OpsCarrier {

    type GADT
    type Cons[H, T <: GADT] <: GADT { val head: H; val tail: T  }
    type Nil <: GADT
  }

  

  object AnyItemOps {
    type using[C <: OpsCarrier] = AnyItemOps {  type Carrier = C  }
  }
  trait AnyItemOps { type Carrier <: OpsCarrier 
    val c: Carrier

    import shapeless.record._
    import scala.language.reflectiveCalls

    @annotation.implicitNotFound(msg = "No field ${K} in record ${S}")
    trait Selector[S <: c.GADT, K] {
      type Out
      def apply(l: S): Out
    }

    trait LowPrioritySelector {
    type Aux[S <: c.GADT, K, Out0] = Selector[S, K] { type Out = Out0 }

    implicit def setSelect[H, T <: c.GADT, K]
      (implicit st : Selector[T, K]): Aux[c.Cons[H,T], K, st.Out] =
        new Selector[c.Cons[H,T], K] {
          type Out = st.Out
          def apply(l : c.Cons[H,T]): Out = st(l.tail)
        }
    }

    object Selector extends LowPrioritySelector {
      def apply[S <: c.GADT, K](implicit selector: Selector[S, K]): Aux[S, K, selector.Out] = selector

      implicit def setSelect1[K, V, T <: c.GADT]: Aux[c.Cons[FieldType[K,V],T], K, V] =
        new Selector[c.Cons[FieldType[K, V], T], K] {
          type Out = V
          def apply(l: c.Cons[FieldType[K, V],T]): Out = l.head
        }
    }
  }

  class ItemOps[C <: OpsCarrier](val c: C) extends AnyItemOps {
    type Carrier = C
  }

  trait AnyItemSyntax {

    type Ops <: AnyItemOps
    val ops: Ops
    type GADT = ops.c.GADT
    implicit def itemOps[S <: GADT](s: S): Syntax[S] = new Syntax(s)

    final class Syntax[S <: GADT](val l: S) {
      import shapeless.Witness
      import shapeless.syntax.singleton._
      import ops._
      def get(k: Witness)(implicit selector: ops.Selector[S, k.T]): selector.Out = selector(l)
    }
  }
  class ItemSyntax[O <: AnyItemOps](val ops: O) extends AnyItemSyntax { type Ops = O }

  // now let's implement this
  case object TypeSetGADT extends OpsCarrier {
    type GADT = TypeSet
    type Cons[H, T <: GADT] = :~:[H,T]
    type Nil = ∅
  }
  case object TypeSetItemOps extends ItemOps(TypeSetGADT)
  case object TypeSetSyntax extends ItemSyntax(TypeSetItemOps)

  // now shapeless
  case object HListGADT extends OpsCarrier {
    import shapeless._
    type GADT = HList
    type Cons[H, T <: GADT] = ::[H,T]
    type Nil = HNil
  }
  case object HListItemOps extends ItemOps(HListGADT)
  case object HListSyntax extends ItemSyntax(HListItemOps)

}



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

