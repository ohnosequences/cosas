package ohnosequences.typesets

trait Predicate

trait AnyFn {

  type Out
}



object AnyFn {

  type withCodomain[Z] = AnyFn { type Out <: Z }
  type constant[Z] = AnyFn { type Out = Z }

  trait WithCodomain[Z] extends AnyFn {

    type Out <: Z
  }
}

trait Out[Z] {

  type Out <: Z
}

/// yuhu


trait AnyFn1 extends AnyFn {

  type I1

  type Out
}

trait Fn1[X] extends AnyFn1 {

  type I1 = X
}

object AnyFn1 {

  type withOutBoundedBy[Z] = AnyFn1 with Out[Z]
  type ⊢[I,O] = AnyFn1 { type I1 = I; type Out = O }
}

trait AnyFn2 extends AnyFn {

  type I1; type I2

  type Out
}

trait Fn2[X,Y] extends AnyFn2 {

  type I1 = X; type I2 = Y
}

object AnyFn2 {

  type withOutBoundedBy[Z] = AnyFn2 with Out[Z]
}

trait AnyFn3 extends AnyFn {

  type I1; type I2; type I3

  type Out
}

trait Fn3[A,B,C] extends AnyFn3 {

  type I1 = A; type I2 = B; type I3 = C
}

object AnyFn3 {

  type withOutBoundedBy[Z] = AnyFn3 with Out[Z]
}