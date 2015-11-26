package ohnosequences.cosas

final case class NotSubtypeOf[A, B] private[cosas](val witness: NotSubtypeOf.type) extends AnyVal

case object NotSubtypeOf extends WorksIfNoAmbiguity {

  implicit def nsubAmbig1[A, B >: A]: A NotSubtypeOf B = sys.error("WTF")
  implicit def nsubAmbig2[A, B >: A]: A NotSubtypeOf B = sys.error("WTF")
}
trait WorksIfNoAmbiguity {

  implicit def nsub[A, B]: A NotSubtypeOf B = new (A NotSubtypeOf B)(NotSubtypeOf)
}

final case class Distinct[A, B] private[cosas](val witness: Distinct.type) extends AnyVal

case object Distinct extends AllDistinctIfNoAmbiguity {

  implicit def neqAmbig1[A]: A != A = sys.error("WTF")
  implicit def neqAmbig2[A]: A != A = sys.error("WTF")
}
trait AllDistinctIfNoAmbiguity {

  implicit def neq[A, B] : A != B = new (A != B)(Distinct)
}

final case class SubtypeOf[A, B] private[cosas](val witness: SubtypeOf.type) extends AnyVal {

  def asRight(a : A): A with B = a.asInstanceOf[A with B]
}

case object SubtypeOf extends WorksForSubtypesToo {

  implicit def refl[A]: A SubtypeOf A = new (A SubtypeOf A)(this)
}

trait WorksForSubtypesToo {

  implicit def subtype[A, B <: A]: B SubtypeOf A = new (B SubtypeOf A)(SubtypeOf)
}
