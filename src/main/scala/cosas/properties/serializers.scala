package ohnosequences.cosas.properties

import ohnosequences.cosas.types._

trait AnyPropertySerializer extends AnyDenotationSerializer {

  type Type <: AnyProperty
  type D = Type#Raw
}

case class PropertySerializer[P <: AnyProperty,V](
  val tpe: P,
  val labelRep: String
)(
  val serializer: P#Raw => Option[V]
) extends AnyPropertySerializer {

  type Type = P
  type Value = V
}
