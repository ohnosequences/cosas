package ohnosequences.typesets

trait AnyFn2 {

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
