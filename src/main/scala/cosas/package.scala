package ohnosequences

package object cosas {

  sealed trait <:!<[A, B]

  implicit def nsub[A, B] : A <:!< B = new <:!<[A, B] {}
  implicit def nsubAmbig1[A, B >: A] : A <:!< B = ???
  implicit def nsubAmbig2[A, B >: A] : A <:!< B = ???

  case object TypeRef
  case class TypeRef[X](val x: TypeRef.type) extends AnyVal

  def q[X]: TypeRef[X] = TypeRef(TypeRef)
}
