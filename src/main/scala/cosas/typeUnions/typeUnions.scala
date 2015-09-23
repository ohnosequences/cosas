package ohnosequences.cosas.typeUnions

/*
  The two type-level constructors of a type union.
  A generic term looks like `either[A]#or[B]#or[C]`.
*/
trait AnyTypeUnion {

  type or[Y] <: AnyTypeUnion
  type union // kind of return
}

sealed trait either[X] extends TypeUnion[not[X]] {}

/* Builder */
trait TypeUnion[T] extends AnyTypeUnion {

  type or[S] = TypeUnion[T with not[S]]
  type union = not[T]
}

case object TypeUnion {
  type empty = either[Nothing]
}
