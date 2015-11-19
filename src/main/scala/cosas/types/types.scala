package ohnosequences.cosas.types

import ohnosequences.cosas.fns._

trait AnyType {

  type Raw
  val label: String

  final type Me = this.type
  implicit final val justMe: Me = this
}

case object AnyType {

  type withRaw[V] = AnyType { type Raw = V }

  implicit def typeSyntax[T <: AnyType](tpe: T): syntax.TypeSyntax[T] = syntax.TypeSyntax(tpe)
}

class Type[V](val label: String) extends AnyType { type Raw = V }


case object typeLabel extends DepFn1[AnyType, String] {

  implicit def atType[T <: AnyType]: AnyApp1At[typeLabel.type,T] { type Y = String } = typeLabel at { t: T => t.label }
}
