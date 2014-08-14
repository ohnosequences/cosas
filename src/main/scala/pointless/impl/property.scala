package ohnosequences.pointless.impl

import scala.reflect.ClassTag

object property extends ohnosequences.pointless.property {

  // wire deps
  type representable = ohnosequences.pointless.impl.representable.type
  import ohnosequences.pointless.impl.representable._

  type Property = ohnosequences.pointless.AnyProperty

  implicit def getOps[P <: Property](p: P)
  (implicit 
    repOps: P => RepresentableOpsImpl[P]
  )
  : PropertyOpsImpl[P] = PropertyOpsImpl(p)

  case class PropertyOpsImpl[P <: Property](val p: P)(implicit getOpsRep: P => RepresentableOpsImpl[P])
  extends PropertyOps[P](p) {

    def label = property.label
    def classTag: ClassTag[_ <: P#Raw] = property.classTag
  }

  class property[V](val label: String)(implicit val classTag: ClassTag[V]) extends ohnosequences.pointless.AnyProperty {

    type Raw = V
  }
}
