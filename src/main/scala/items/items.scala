package ohnosequences.typesets.items

import ohnosequences.typesets._

// the idea is that
// 
//    1. Syntax needs Ops
//    2. for Ops you need a Carrier
//    3. for providing Ops, you need Syntax
//    4. and the carrier bounds a particular GADT "shape"
// 
// I want to check whether is possible to abstract over the type for which syntax
// is provided. In this case, FieldType? FieldOf?
object experiment {

  trait AnyCarrier {

    type GADT
    type Cons[H, T <: GADT] <: GADT { val head: H; val tail: T  }
    type Nil <: GADT
  }

  object AnyOps {
    type using[C <: AnyCarrier] = AnyOps {  type Carrier = C  }
  }
  trait AnyOps { 

    type Carrier <: AnyCarrier 
    val carrier: Carrier

    // this is really ops, the rest is ops implementation
    @annotation.implicitNotFound(msg = "No field ${K} in record ${S}")
    trait Selector[S <:  carrier.GADT, K] {
      type Out
      def apply(l: S): Out
    }

    import shapeless.record._
    import scala.language.reflectiveCalls

    trait LowPrioritySelector {
    type Aux[S <: carrier.GADT, K, Out0] = Selector[S, K] { type Out = Out0 }

    implicit def setSelect[H, T <:  carrier.GADT, K]
      (implicit st : Selector[T, K]): Aux[ carrier.Cons[H,T], K, st.Out] =
        new Selector[ carrier.Cons[H,T], K] {
          type Out = st.Out
          def apply(l :  carrier.Cons[H,T]): Out = st(l.tail)
        }
    }

    object Selector extends LowPrioritySelector {
      def apply[S <:  carrier.GADT, K](implicit selector: Selector[S, K]): Aux[S, K, selector.Out] = selector

      implicit def setSelect1[K, V, T <:  carrier.GADT]: Aux[ carrier.Cons[FieldType[K,V],T], K, V] =
        new Selector[ carrier.Cons[FieldType[K, V], T], K] {
          type Out = V
          def apply(l:  carrier.Cons[FieldType[K, V],T]): Out = l.head
        }
    }
  }

  class Ops[C <: AnyCarrier](val carrier: C) extends AnyOps {
    type Carrier = C
  }

  trait AnySyntax {

    type Ops <: AnyOps
    val ops: Ops
    type GADT = ops.carrier.GADT
    implicit def itemOps[S <: GADT](s: S): API[S] = new API(s)

    final class API[S <: GADT](val l: S) {
      import shapeless.Witness
      import shapeless.syntax.singleton._
      import ops._
      def get(k: Witness)(implicit selector: ops.Selector[S, k.T]): selector.Out = selector(l)
    }
  }
  class Syntax[O <: AnyOps](val ops: O) extends AnySyntax { type Ops = O }

  // now let's implement this
  case object TypeSetCarrier extends AnyCarrier {
    type GADT = TypeSet
    type Cons[H, T <: GADT] = :~:[H,T]
    type Nil = ∅
  }
  case object TypeSetOps extends Ops(TypeSetCarrier)
  case object TypeSetSyntax extends Syntax(TypeSetOps)

  // now shapeless
  case object HListCarrier extends AnyCarrier {
    import shapeless._
    type GADT = HList
    type Cons[H, T <: GADT] = ::[H,T]
    type Nil = HNil
  }
  case object HListOps extends Ops(HListCarrier)
  case object HListSyntax extends Syntax(HListOps)

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

