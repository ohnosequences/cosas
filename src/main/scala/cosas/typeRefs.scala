package ohnosequences.cosas

// these are used to bound a type. You can simulate multiple type parameter lists this way.
case object TypeRef
case class TypeRef[X](val x: TypeRef.type) extends AnyVal
