package ohnosequences.typesets

import shapeless._, poly._
  
/* Mapping a set to another set, i.e. the results of mapping should have distinct types */  
@annotation.implicitNotFound(msg = "Can't map ${F} over ${In} (maybe the resulting types are not distinct)")
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
      e: OutH ∉ OutT  // the key check here
    ): Aux[F, H :~: T, OutH :~: OutT] =
      new SetMapper[F, H :~: T] { type Out = OutH :~: OutT
        def apply(s: H :~: T): Out = h(s.head) :~: t(s.tail)
      }
}


/* Mapping a set to an HList: when you want to preserve precise types, but they are not distinct */
@annotation.implicitNotFound(msg = "Can't map ${F} over ${In} to an HList")
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

/* Mapping a set to a List: normally, when you are mapping everything to one type */
@annotation.implicitNotFound(msg = "Can't map ${F} over ${In} to a List")
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
  
  implicit def oneListMapper[F <: Poly, OH, H]
    (implicit h: Case1.Aux[F, H, OH]): Aux[F, H :~: ∅, OH] =
      new ListMapper[F, H :~: ∅] { type O = OH
        def apply(s: H :~: ∅): Out = List[OH](h(s.head))
      }

  implicit def consListMapper[F <: Poly, O_, H1, H2, T <: TypeSet]
    (implicit
      h: Case1.Aux[F, H1, O_], 
      t: Aux[F, H2 :~: T, O_]
    ): Aux[F, H1 :~: H2 :~: T, O_] =
      new ListMapper[F, H1 :~: H2 :~: T] { type O = O_
        def apply(s: H1 :~: H2 :~: T): Out = h(s.head) :: t(s.tail)
      }
}
