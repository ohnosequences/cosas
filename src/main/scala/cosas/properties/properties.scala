package ohnosequences.cosas.properties

// deps
import ohnosequences.cosas.types._, ohnosequences.cosas.typeSets._

trait AnyProperty extends AnyType

class Property[V](val label: String) extends AnyProperty { type Raw = V }

case object AnyProperty {

  type ofType[T] = AnyProperty { type Raw = T }
}
