package ohnosequences.typesets

import shapeless._, poly._
  
trait SetMapper[F, In <: TypeSet] extends DepFn1[In] { type Out <: TypeSet }

object SetMapper {
  def apply[F, S <: TypeSet](implicit mapper: SetMapper[F, S]): Aux[F, S, mapper.Out] = mapper

  type Aux[F, In <: TypeSet, O <: TypeSet] = SetMapper[F, In] { type Out = O }
  
  implicit def emptyMapper[F]: Aux[F, ∅, ∅] =
    new SetMapper[F, ∅] {
      type Out = ∅
      def apply(s: ∅): Out = ∅
    }
  
  implicit def consMapper[F <: Poly, H, OutH, T <: TypeSet, OutT <: TypeSet]
    (implicit
      h: Case1.Aux[F, H, OutH], 
      t: Aux[F, T, OutT],
      e: OutH ∉ OutT
    ): Aux[F, H :~: T, OutH :~: OutT] =
      new SetMapper[F, H :~: T] { type Out = OutH :~: OutT
        def apply(s: H :~: T): Out = h(s.head) :~: t(s.tail)
      }
}

trait HListMapper[F, In <: TypeSet] extends DepFn1[In] { type Out <: HList }

object HListMapper {
  def apply[F, S <: TypeSet](implicit mapper: HListMapper[F, S]): Aux[F, S, mapper.Out] = mapper

  type Aux[F, In <: TypeSet, O <: HList] = HListMapper[F, In] { type Out = O }
  
  implicit def emptyHListMapper[F]: Aux[F, ∅, HNil] =
    new HListMapper[F, ∅] {
      type Out = HNil
      def apply(s: ∅): Out = HNil
    }
  
  implicit def consHListMapper[F <: Poly, H, OutH, T <: TypeSet, OutT <: HList]
    (implicit
      h: Case1.Aux[F, H, OutH], 
      t: Aux[F, T, OutT]
    ): Aux[F, H :~: T, OutH :: OutT] =
      new HListMapper[F, H :~: T] { type Out = OutH :: OutT
        def apply(s: H :~: T): Out = h(s.head) :: t(s.tail)
      }
}

trait ListMapper[F, In <: TypeSet] extends DepFn1[In] { 
  type O
  type Out = List[O]
}

object ListMapper {
  def apply[F, S <: TypeSet](implicit mapper: ListMapper[F, S]): Aux[F, S, mapper.O] = mapper

  type Aux[F, In <: TypeSet, O_] = ListMapper[F, In] { type O = O_ }
  
  implicit def emptyListMapper[F, O_]: Aux[F, ∅, O_] =
    new ListMapper[F, ∅] { type O = O_
      def apply(s: ∅): Out = Nil
    }
  
  implicit def consListMapper[F <: Poly, H, OutH, T <: TypeSet]
    (implicit
     h: Case1.Aux[F, H, OutH], 
     t: Aux[F, T, OutH]
    ): Aux[F, H :~: T, OutH] =
      new ListMapper[F, H :~: T] { type O = OutH
        def apply(s: H :~: T): Out = h(s.head) :: t(s.tail)
      }
}
