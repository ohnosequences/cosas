package ohnosequences.cosas.types

import ohnosequences.cosas.fns._

trait AnyFunctionType extends AnyType {

  type Domain <: AnyType
  val domain: Domain

  type Codomain <: AnyType
  val codomain: Codomain

  type Raw = AnyDepFn1 { type In1 = Domain#Raw; type Out = Codomain#Raw }
}

case class ==>[A <: AnyType, B <: AnyType](val domain: A, val codomain: B) extends AnyFunctionType {

  type Domain   = A
  type Codomain = B

  lazy val label: String = s"${domain.label} ==> ${codomain.label}"
}
