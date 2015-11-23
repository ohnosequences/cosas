package ohnosequences.cosas

import fns._

case object test {

  // TODO add syntax for A ≤ B === sub.type isTrueOn (A,B)
  case object sub extends PredicateOver[(Any,Any)] {

    implicit def trueAt[A, B <: A]: sub.type isTrueOn (A,B) = sub.isTrueOn[(A,B)]
  }

  val notSub = ¬(sub)

  case object typeEq extends PredicateOver[(Any,Any)] {

    implicit def trueAt[A]: typeEq.type isTrueOn (A,A) = typeEq.isTrueOn[(A,A)]
  }

  val notTypeEq = ¬(typeEq)

}




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

  implicit def refl[A]: A SubtypeOf A = new (A SubtypeOf A)(this)
}

trait WorksForSubtypesToo {

  implicit def subtype[A, B <: A]: B SubtypeOf A = new (B SubtypeOf A)(SubtypeOf)
}
