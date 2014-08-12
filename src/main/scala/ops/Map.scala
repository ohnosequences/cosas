package ohnosequences.typesets

import shapeless._, poly._
  
/* Mapping a set to another set, i.e. the results of mapping should have distinct types */  
@annotation.implicitNotFound(msg = "Can't map ${F} over ${In} (maybe the resulting types are not distinct)")
trait SetMapper[F <: Poly, In <: TypeSet] extends DepFn1[In] with Fn2[F,In] {

  type Out <: TypeSet

  def apply(s: In): Out
}

object SetMapper {

  def apply[F <: Poly, S <: TypeSet](implicit mapper: SetMapper[F,S]): Aux[F, S, mapper.Out] = mapper

  type Aux[F <: Poly, In <: TypeSet, O <: TypeSet] = SetMapper[F, In] { type Out = O }
  
  implicit def emptyMapper[F <: Poly]: Aux[F, ∅, ∅] = new SetMapper[F, ∅] {
      
      type Out = ∅
      def apply(s: ∅): Out = ∅
    }
  
  implicit def consMapper [
    F <: Poly,
    H <: TypeSet, OutH <: TypeSet,
    T <: TypeSet, OutT <: TypeSet
  ](implicit
    h: Case1.Aux[F, H, OutH], 
    t: Aux[F, T, OutT],
    e: OutH ∉ OutT  // the key check here
  )
  : Aux[F, H :~: T, OutH :~: OutT] = new SetMapper[F, H :~: T] { 

        type Out = OutH :~: OutT
        def apply(s: H :~: T): Out = h(s.head) :~: t(s.tail)
      }
}


/* Mapping a set to an HList: when you want to preserve precise types, but they are not distinct */
@annotation.implicitNotFound(msg = "Can't map ${F} over ${In} to an HList")
trait HListMapper[In <: TypeSet, F <: Poly] extends Fn2[In,F] { 

  type Out <: HList
  def apply(s: In): Out 
}

object HListMapper {

  def apply[S <: TypeSet, F <: Poly](implicit mapper: HListMapper[S, F]): Aux[S, F, mapper.Out] = mapper

  type Aux[In <: TypeSet, F <: Poly, O <: HList] = HListMapper[In, F] { type Out = O }
  
  implicit def emptyHListMapper[F <: Poly]: Aux[∅, F, HNil] =
    new HListMapper[∅, F] {
      type Out = HNil
      def apply(s: ∅): Out = HNil
    }
  
  implicit def consHListMapper[
    H, T <: TypeSet, 
    F <: Poly, 
    OutH, OutT <: HList
  ](implicit
    h: Case1.Aux[F, H, OutH], 
    t: Aux[T, F, OutT]
  ): Aux[H :~: T, F, OutH :: OutT] = new HListMapper[H :~: T, F] { 

    type Out = OutH :: OutT
    def apply(s: H :~: T): Out = h(s.head) :: t(s.tail:T)
  }
}

/* Mapping a set to a List: normally, when you are mapping everything to one type */
@annotation.implicitNotFound(msg = "Can't map ${F} over ${In} to a List")
trait ListMapper[In <: TypeSet, F <: Poly] extends Fn2[In, F] { 

  type O
  type Out = List[O]

  def apply(s: In): Out
}

object ListMapper {

  def apply[S <: TypeSet, F <: Poly](implicit mapper: ListMapper[S, F]): Aux[S, F, mapper.O] = mapper

  type Aux[In <: TypeSet, F <: Poly, O_] = ListMapper[In, F] { type O = O_ }
  
  implicit def emptyListMapper[F <: Poly, O_]: ListMapper[∅,F] with Aux[∅, F, O_] = new ListMapper[∅, F] { 

    // shouldn't be needed
    type O = O_
    def apply(s: ∅): Out = Nil
  }
  
  implicit def oneListMapper[H, F <: Poly, OH](implicit h: Case1.Aux[F, H, OH])
  : Aux[H :~: ∅, F, OH] = new ListMapper[H :~: ∅, F] { 

    type O = OH
    def apply(s: H :~: ∅): Out = List[OH](h(s.head))
  }

  implicit def consListMapper[H1, H2, T <: TypeSet, F <: Poly, O_](implicit
    h: Case1.Aux[F, H1, O_], 
    t: Aux[H2 :~: T, F, O_]
  ): Aux[H1 :~: H2 :~: T, F, O_] = new ListMapper[H1 :~: H2 :~: T, F] { 

    type O = O_
    def apply(s: H1 :~: H2 :~: T): Out = h(s.head) :: t(s.tail)
  }
}
