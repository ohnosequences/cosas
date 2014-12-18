package ohnosequences.cosas

object fn {

  trait AnyFn { type Out }

  trait OutBound[X] extends AnyFn { type Out <: X }
  trait Out[X]      extends AnyFn { type Out = X }

  trait ContainerOut extends AnyFn { type O }
  trait OutInContainer[F[_]] extends ContainerOut { type Out = F[O] }
  trait InContainer[X] extends ContainerOut { type O = X }


  /* Fns with different number of arguments */
  trait AnyFn0 extends AnyFn with shapeless.DepFn0

  trait Fn0 extends AnyFn0


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

}
