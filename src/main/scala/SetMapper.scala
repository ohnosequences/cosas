package ohnosequences.typesets

import shapeless._, poly._
  
trait SetMapper[F, In <: TypeSet] extends DepFn1[In] { type Out <: TypeSet }

object SetMapper {
  def apply[F, L <: TypeSet](implicit mapper: SetMapper[F, L]): Aux[F, L, mapper.Out] = mapper

  type Aux[F, In <: TypeSet, O <: TypeSet] = SetMapper[F, In] { type Out = O }
  
  implicit def emptyMapper[F]: Aux[F, ∅, ∅] =
    new SetMapper[F, ∅] {
      type Out = ∅
      def apply(s : ∅): Out = ∅
    }
  
  implicit def consMapper[F <: Poly, H, OutH, T <: TypeSet, OutT <: TypeSet]
    (implicit
      h: Case1.Aux[F, H, OutH], 
      t: Aux[F, T, OutT],
      e: OutH ∉ OutT
    ): Aux[F, H :~: T, OutH :~: OutT] =
      new SetMapper[F, H :~: T] { type Out = OutH :~: OutT
        def apply(s : H :~: T): Out = h(s.head) :~: t(s.tail)
      }
}
