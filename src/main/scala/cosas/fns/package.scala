package ohnosequences.cosas

package object fns {

  type  as[X,Y >: X] = As[X,Y]
  def   as[X,Y >: X]: as[X,Y] = new as[X,Y]

  implicit def toDepFn1[A, B](f: A => B): Fn1[A, B] = Fn1(f)

  implicit def toDepFn2[A, B, C](f: (A, B) => C): Fn2[A, B, C] = Fn2(f)

  type isTrueOn[P <: AnyPredicate { type In1 >: X }, X] = AnyApp1At[P, X] { type Y = True }
  type isFalseOn[P <: AnyPredicate { type In1 >: X }, X] = AnyApp1At[P, X] { type Y = False }
}
