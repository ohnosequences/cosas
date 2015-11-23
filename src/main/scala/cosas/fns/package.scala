package ohnosequences.cosas

package object fns {

  implicit def toDepFn1[A, B](f: A => B): Fn1[A, B] = Fn1(f)
  implicit def toDepFn2[A, B, C](f: (A, B) => C): Fn2[A, B, C] = Fn2(f)

  type isTrueOn[P <: AnyPredicate, X <: P#In1] = AnyApp1At[P, X] { type Y = True }
  type isFalseOn[P <: AnyPredicate, X <: P#In1] = AnyApp1At[P, X] { type Y = False }
  def ¬[P <: AnyPredicate](p: P): Not[P] = Not(p)
}
