package ohnosequences.typesets

trait Predicate

trait AnyFn {

  type Out
}

object AnyFn {

  type withCodomain[Z] = AnyFn { type Out <: Z }
  type constant[Z] = AnyFn { type Out = Z }
}

trait AnyFn1 extends AnyFn {

  type I1

  type Out
}

trait Fn1[X] extends AnyFn1 {

  type I1 = X
}

object AnyFn1 {

  type withOutBoundedBy[Z] = AnyFn1 with Out[Z]
}

trait AnyFn2 extends AnyFn {

  type I1
  type I2

  type Out
}
trait Fn2[X,Y] extends AnyFn2 {

  type I1 = X
  type I2 = Y

  type Out
}

trait Out[Z] {

  type Out <: Z
}

object AnyFn2 {

  type withOutBoundedBy[Z] = AnyFn2 with Out[Z]
}
