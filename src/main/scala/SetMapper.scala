package ohnosequences.typesets

import shapeless._
import poly._
  
trait SetMapper[F, In <: TypeSet] extends DepFn1[In] { type Out <: TypeSet }

object SetMapper {
  def apply[F, L <: TypeSet](implicit mapper: SetMapper[F, L]): Aux[F, L, mapper.Out] = mapper

  type Aux[F, In <: TypeSet, O <: TypeSet] = SetMapper[F, In] { type Out = O }
  
  implicit def emptyMapper[F]: Aux[F, ∅, ∅] =
    new SetMapper[F, ∅] {
      type Out = ∅
      def apply(s : ∅): Out = ∅
    }
  
  implicit def consMapper[F <: Poly, H, T <: TypeSet, OutH, OutT <: TypeSet]
    (implicit hc: Case1.Aux[F, H, OutH], mt: Aux[F, T, OutT]): 
    // TODO: add this constraint: `OutH ∉ OutT` (or control in some other way, that the output is a TypeSet as well)
      Aux[F, H :~: T, hc.Result :~: mt.Out] =
        new SetMapper[F, H :~: T] { type Out = hc.Result :~: mt.Out
          def apply(s : H :~: T): Out = hc(s.head) :~: mt(s.tail)
        }
}
