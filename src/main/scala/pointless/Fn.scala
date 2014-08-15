package ohnosequences.pointless


trait AnyFn {

  type Out
}

object AnyFn {
  
  type out[T] = AnyFn { type Out = T }

  trait Constant[Z] extends AnyFn { 

    type Out = Z 
  }

  trait WithCodomain[Z] extends AnyFn {

    type Out <: Z
  }

  trait Wrapped extends AnyFn {

    type O
  }

  type o[T] = Wrapped { type O = T }

  trait WrappedIn[F[_]] extends Wrapped{

    type Out = F[O]
  }
  
  trait ConstantWrappedIn[O0,F[_]] extends WrappedIn[F] {

    type O = O0
  }

  trait Out[Z] {

    type Out <: Z
  }

  trait AnyFn1 extends AnyFn {

    type In1

    def apply(in1: In1): Out
  }

  trait Fn1[A] extends AnyFn1 {

    type In1 = A
  }

  trait AnyFn2 extends AnyFn {

    type In1; type In2

    def apply(in1: In1, in2: In2): Out
  }

  trait Fn2[A, B] extends AnyFn2 {

    type In1 = A; type In2 = B
  }

  trait AnyFn3 extends AnyFn {

    type In1; type In2; type In3

    def apply(in1: In1, in2: In2, in3: In3): Out
  }

  trait Fn3[A, B, C] extends AnyFn3 {

    type In1 = A; type In2 = B; type In3 = C
  }

}
