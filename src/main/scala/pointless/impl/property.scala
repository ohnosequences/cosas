package ohnosequences.pointless.impl

import scala.reflect.ClassTag

object property extends ohnosequences.pointless.property {

  // wire deps
  type representable = ohnosequences.pointless.impl.representable.type
  import ohnosequences.pointless.impl.representable._

  type Property = AnyProperty

  implicit def getOps[P <: Property](p: P)
  (implicit 
    repOps: P => RepresentableOpsImpl[P]
  )
  : PropertyOpsImpl[P] = PropertyOpsImpl(p)

  case class PropertyOpsImpl[P <: Property](p: P)(implicit getOpsRep: P => RepresentableOpsImpl[P])
  extends PropertyOps[P](p)

  trait AnyProperty extends Representable {

    val label: String
    val classTag: ClassTag[Raw]
  }

  class property[V](val label: String)(implicit val classTag: ClassTag[V]) extends AnyProperty {

    type Raw = V
  }
}