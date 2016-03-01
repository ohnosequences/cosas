package ohnosequences.cosas.elgot

case class Elgot[I,U,O](
  val init: I ⇒ Either[U,O],
  val iter: U ⇒ Either[U,O]
) { me =>

  def andThen[V,A](next: Elgot[O, V, A]): Elgot[I, Either[U,V], A] = Elgot(
    init = { x: I =>
      me.init(x) match {
        case Left(u) => Left(Left(u))
        case Right(o) => next.init(o) match {
          case Left(v) => Left(Right(v))
          case Right(a) => Right(a)
        }
      }
    },
    iter = { uv: Either[U,V] =>
      uv match {
        case Left(u) => me.iter(u) match {
          case Left(u) => Left(Left(u))
          case Right(o) => next.init(o) match {
            case Left(v) => Left(Right(v))
            case Right(a) => Right(a)
          }
        }
        case Right(v) => next.iter(v) match {
          case Right(a) => Right(a)
          case Left(v) => Left(Right(v))
        }
      }
    }
  )
}



case object Elgot {

  implicit def tailrecSyntax[I,U,O](e: Elgot[I,U,O]): Tailrec[I,U,O] =
    Tailrec(e)
}

case class Tailrec[I,U,O](val elgot: Elgot[I,U,O]) extends AnyVal {

  final def tailrec: I ⇒ O = {

    @scala.annotation.tailrec
    def recurse_aux(u: U): O = elgot.iter(u) match {

      case Right(o) => o
      case Left(u)  => recurse_aux(u)
    }

    i: I ⇒ elgot.init(i) match {
      case Right(o) => o
      case Left(u)  => recurse_aux(u)
    }
  }

  final def fun(x: I): O = {

    var res = elgot.init(x)

    while(res.isLeft) {
      res = elgot.iter(res.left.get)
    }

    res.right.get
  }
}
