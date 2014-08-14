package ohnosequences.pointless.impl

import scala.reflect.ClassTag

object property extends ohnosequences.pointless.property {

  // wire deps
  type representable = ohnosequences.pointless.impl.representable.type
  val representable = ohnosequences.pointless.impl.representable

  type Property = AnyProperty

  implicit def getOps[P <: Property](p: P)
  (implicit 
    repOps: representable.ops[P]
  )
  : ops[P] = ops(p)

  trait AnyProperty extends representable.Representable {

    val label: String
    val classTag: ClassTag[Raw]
  }
}