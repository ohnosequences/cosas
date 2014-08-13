package ohnosequences.pointless

trait AnyFn {

  type Out
}

object AnyFn {
  
  trait Constant[Z] extends AnyFn { 

    type Out = Z 
  }

  trait WithCodomain[Z] extends AnyFn {

    type Out <: Z
  }

  trait WrappedIn[F[_]] {

    type O
    type Out = F[O]
  }
  
  trait ConstantWrappedIn[O0,F[_]] extends WrappedIn[F] {

    type O = O0
  }
}

trait Out[Z] {

  type Out <: Z
}

trait AnyFn1 extends AnyFn {

  type I1

  type Out
}

trait Fn1[X] extends AnyFn1 {

  type I1 = X
}

trait AnyFn2 extends AnyFn {

  type I1; type I2

  type Out
}

trait Fn2[X,Y] extends AnyFn2 {

  type I1 = X; type I2 = Y
}

trait AnyFn3 extends AnyFn {

  type I1; type I2; type I3

  type Out
}

trait Fn3[A,B,C] extends AnyFn3 {

  type I1 = A; type I2 = B; type I3 = C
}