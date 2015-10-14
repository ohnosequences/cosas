package ohnosequences.cosas

// TODO consider caching singleton

sealed trait !<[A,B]

case object !< extends SubtypeYieldsAmbiguity {

  implicit def nsub[A, B] : A !< B = new !<[A, B] {}
}

trait SubtypeYieldsAmbiguity {

  implicit def nsubAmbig1[A, B >: A] : A !< B = throw new Exception {}
  implicit def nsubAmbig2[A, B >: A] : A !< B = throw new Exception {}
}

sealed trait !=[A, B]

case object != extends EqualTypesYieldsAmbiguity {

  implicit def neq[A, B] : A != B = new !=[A, B] {}
}

trait EqualTypesYieldsAmbiguity {

  implicit def neqAmbig1[A] : A != A = throw new Exception {}
  implicit def neqAmbig2[A] : A != A = throw new Exception {}
}

sealed trait ≤[A,B]

case object ≤ extends WorksForSubtypesToo {

  implicit def refl[A]: A ≤ A = new (A ≤ A) {}
}

trait WorksForSubtypesToo {

  implicit def subtype[A, B <: A]: B ≤ A = new (B ≤ A) {}
}
