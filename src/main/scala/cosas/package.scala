package ohnosequences

package object cosas {

  sealed trait !<[A,B]

  case object !< {

    implicit def nsub[A, B] : A !< B = new !<[A, B] {}
    implicit def nsubAmbig1[A, B >: A] : A !< B = throw new Exception
    implicit def nsubAmbig2[A, B >: A] : A !< B = throw new Exception
  }

  trait !=[A, B]

  case object != {

    implicit def neq[A, B] : A != B = new !=[A, B] {}
    implicit def neqAmbig1[A] : A != A = throw new Exception
    implicit def neqAmbig2[A] : A != A = throw new Exception
  }

  case object TypeRef
  case class TypeRef[X](val x: TypeRef.type) extends AnyVal

  def ofType[X]: TypeRef[X] = TypeRef(TypeRef)
}
