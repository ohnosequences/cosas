package ohnosequences.cosas.elgot

case class Elgot[I,U,O](
  val init: I ⇒ Either[U,O],
  val iter: U ⇒ Either[U,O]
)

case object Elgot {

  implicit def tailrecSyntax[I,U,O](e: Elgot[I,U,O]): Tailrec[I,U,O] =
    Tailrec(e)
}

case class Tailrec[I,U,O](val elgot: Elgot[I,U,O]) extends AnyVal {

  def tailrec: I ⇒ O = {

    @scala.annotation.tailrec
    final def recurse_aux(u: U): O = elgot.iter(u) match {

      case Right(o) => o
      case Left(u)  => recurse_aux(u)
    }

    i: I ⇒ elgot.init(i) match {
      case Right(o) => o
      case Left(u)  => recurse_aux(u)
    }
  }
}
