package ohnosequences.cosas.fns

import ohnosequences.cosas._

trait AnyFn1 extends Any with AnyDepFn1 {

  def f: In1 => Out
}
// std functions as depfns
case class Fn1[A,B](val f: A => B) extends AnyVal with AnyFn1 with DepFn1[A,B]

case object Fn1 {

  implicit def defaultApp[A,B](df: Fn1[A,B]): AnyApp1At[DepFn1[A,B], A] { type Y = B } =
    AppFn1(df.f)

  case class AppFn1[A,B](val f: A => B) extends AnyVal with AnyApp1At[DepFn1[A,B],A] {

    type Y = B

    def apply(x: A): B = f(x)
  }
}

trait AnyFn2 extends Any with AnyDepFn2 {

  def f: (In1,In2) => Out
}
case class Fn2[A, B, C](val f: (A,B) => C) extends AnyVal with AnyFn2 with DepFn2[A, B, C]

case object Fn2 {

  implicit def defaultApp[A, B, C](df: Fn2[A, B, C]):
    AnyApp2At[Fn2[A, B, C], A, B] { type Y = C } = App2 { (a: A, b: B) => df.f(a, b) }
}

case object identity extends DepFn1[Any, Any] {

  implicit def default[X]: AnyApp1At[identity.type,X] { type Y = X } = identity at { x: X => x }
}

class as[X, Y >: X] extends DepFn1[X,Y]

case object as {

  def apply[X,Y >: X]: as[X,Y] = new as[X,Y]

  implicit def default[A, B >: A]: AnyApp1At[as[A,B],A] {type Y = B } = App1 { a: A => a }
}
