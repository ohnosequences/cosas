// package ohnosequences.cosas.properties
//
// import ohnosequences.cosas.types._
//
// trait AnyPropertySerializer extends AnyDenotationSerializer {
//
//   type Type <: AnyProperty
// }
//
// case class PropertySerializer[P <: AnyProperty,V](
//   val _tpe: P,
//   val _labelRep: String
// )(
//   val _serializer: P#Raw => Option[V]
// ) extends DenotationSerializer[P,P#Raw,V](_tpe,_labelRep)(_serializer) with AnyPropertySerializer
