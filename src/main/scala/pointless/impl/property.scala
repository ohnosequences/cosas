package ohnosequences.pointless.impl

import scala.reflect.ClassTag

trait AnyProperty extends ohnosequences.pointless.AnyProperty with AnyRepresentable {

  val label: String
  val classTag: ClassTag[Raw]
}

class Property[V](val label: String)(implicit val classTag: ClassTag[V]) extends AnyProperty {

  type Raw = V
}

object AnyProperty {

  implicit def propertyOps[P <: AnyProperty](p: P): PropertyOps[P] = PropertyOps[P](p)
  case class   PropertyOps[P <: AnyProperty](p: P)
    extends ohnosequences.pointless.AnyProperty.Ops[P](p)(AnyRepresentable.representableOps) {

    def label = property.label
    def classTag: ClassTag[_ <: P#Raw] = property.classTag
  }

}
