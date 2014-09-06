package ohnosequences.pointless


trait AnyFn { type Out }

trait OutBound[X] extends AnyFn { type Out <: X }
trait Out[X]      extends AnyFn { type Out = X }

trait OutWrapped extends AnyFn { type O }
trait OutWrappedIn[F[_]] extends OutWrapped { type Out = F[O] }
trait Wrapped[X] extends OutWrapped { type O = X }

object AnyFn {
  
  // "soft" refinements:
  type out[T] = AnyFn { type Out = T }
  type wrapped[T] = OutWrapped { type O = T }
}

/* Fns with different number of arguments */

trait AnyFn0 extends AnyFn with shapeless.DepFn0


trait AnyFn1 extends AnyFn {

  type In1

  def apply(in1: In1): Out
}

trait Fn1[A] extends AnyFn1 with shapeless.DepFn1[A] { type In1 = A }


trait AnyFn2 extends AnyFn {

  type In1; type In2

  def apply(in1: In1, in2: In2): Out
}

trait Fn2[A, B] extends AnyFn2 with shapeless.DepFn2[A, B] { type In1 = A; type In2 = B }


trait AnyFn3 extends AnyFn {

  type In1; type In2; type In3

  def apply(in1: In1, in2: In2, in3: In3): Out
}

trait Fn3[A, B, C] extends AnyFn3 { type In1 = A; type In2 = B; type In3 = C }
