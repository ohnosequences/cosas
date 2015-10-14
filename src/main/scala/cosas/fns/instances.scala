package ohnosequences.cosas.fns

// std functions as depfns
case class Fn1[A,B](val f: A => B) extends DepFn1[A,B]

case object Fn1 {

  implicit def appForFn1[A,B](fn: Fn1[A,B]): AppFn1[A,B] =
    AppFn1(fn.f)

  implicit def toFn1[A,B](f: A => B): Fn1[A,B] = Fn1(f)

  case class AppFn1[A,B](val f: A => B) extends AnyVal with AnyApp1At[Fn1[A,B],A] {

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
