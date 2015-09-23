package ohnosequences.cosas.properties

import ohnosequences.cosas.types._

trait AnyPropertyParser extends AnyDenotationParser { parser =>

  type Type <: AnyProperty
  type D = Type#Raw
}
case class PropertyParser[P <: AnyProperty,V](
  val tpe: P,
  val labelRep: String)(
  val parser: V => Option[P#Raw]
) extends AnyPropertyParser {

  type Type = P
  type Value = V
}
