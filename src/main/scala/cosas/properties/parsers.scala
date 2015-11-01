// // package ohnosequences.cosas.properties
//
// import ohnosequences.cosas.types._
//
// trait AnyPropertyParser extends AnyDenotationParser { parser =>
//
//   type Type <: AnyProperty
// }
//
// case class PropertyParser[P <: AnyProperty,V](
//   val _tpe: P,
//   val _labelRep: String
// )(
//   val _parser: V => Option[P#Raw]
// ) extends DenotationParser[P,P#Raw,V](_tpe,_labelRep)(_parser) with AnyPropertyParser
