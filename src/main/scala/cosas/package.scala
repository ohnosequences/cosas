package ohnosequences

package object cosas {

  sealed trait <:!<[A, B]

  implicit def nsub[A, B] : A <:!< B = new <:!<[A, B] {}
  implicit def nsubAmbig1[A, B >: A] : A <:!< B = ???
  implicit def nsubAmbig2[A, B >: A] : A <:!< B = ???
}
