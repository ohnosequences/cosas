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
}

case object typeLabel extends DepFn1[AnyType, String] {

  implicit def atType[T <: AnyType]: App1[typeLabel.type,T,String] = typeLabel at { t: T => t.label }
}
