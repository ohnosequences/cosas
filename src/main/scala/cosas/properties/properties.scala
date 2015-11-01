// package ohnosequences.cosas.properties
//
// import ohnosequences.cosas._, types._, typeSets._
//
// trait AnyProperty extends AnyType
//
// class Property[V](val label: String) extends AnyProperty { type Raw = V }
//
// case object AnyProperty {
//
//   type ofType[T] = AnyProperty { type Raw = T }
//
//   implicit def propertySyntax[P <: AnyProperty](p: P): syntax.PropertySyntax[P] =
//     syntax.PropertySyntax(p)
// }
