package ohnosequences.cosas.fns

trait AnyFn1 extends Any with AnyDepFn1 {

  type In1
  type Out

  def f: In1 => Out
}
// std functions as depfns
case class Fn1[A,B](val f: A => B) extends AnyVal with AnyFn1 with DepFn1[A,B]

case object Fn1 {

  implicit def appForFn1[A,B](df: Fn1[A,B]): AnyApp1At[DepFn1[A,B], A] =
    AppFn1(df.f)

  case class AppFn1[A,B](val f: A => B) extends AnyVal with AnyApp1At[DepFn1[A,B],A] {

    type Y = B

    def apply(x: A): B = f(x)
  }
}

case object identity extends DepFn1[Any, Any] {

  implicit def default[X <: In1]: App1[identity.type,X,X] = identity at { x: X => x }
}

class As[X, Y >: X] extends DepFn1[X,Y]

case object As {

  implicit def default[A, B >: A]: App1[As[A,B],A,B] = App1 { a: A =>  a}
}
