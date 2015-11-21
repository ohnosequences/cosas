package ohnosequences.cosas

// NOTE all these could be predicates on witnesses?
final case class NotSubtypeOf[A,B] private(val witness: NotSubtypeOf.type) extends AnyVal

case object NotSubtypeOf extends SubtypeYieldsAmbiguity {

  implicit def nsub[A, B]: A !< B = new (A !< B)(this)
}

trait SubtypeYieldsAmbiguity {

  implicit def nsubAmbig1[A, B >: A]: A !< B = throw new Exception {}
  implicit def nsubAmbig2[A, B >: A]: A !< B = throw new Exception {}
}

final case class Distinct[A, B] private(val witness: Distinct.type) extends AnyVal

case object Distinct extends EqualTypesYieldsAmbiguity {

  implicit def neq[A, B] : A != B = new (A != B)(this)
}

trait EqualTypesYieldsAmbiguity {

  implicit def neqAmbig1[A]: A != A = throw new Exception {}
  implicit def neqAmbig2[A]: A != A = throw new Exception {}
}

final case class SubtypeOf[A,B] private[cosas](val witness: SubtypeOf.type) extends AnyVal {

  def asRight(a : A): A with B = a.asInstanceOf[A with B]
}

case object SubtypeOf extends WorksForSubtypesToo {

  implicit def refl[A]: A ≤ A = new (A ≤ A)(this)
}

trait WorksForSubtypesToo {

  implicit def subtype[A, B <: A]: B ≤ A = new (B ≤ A)(SubtypeOf)
}
