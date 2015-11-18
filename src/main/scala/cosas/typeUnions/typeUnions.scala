package ohnosequences.cosas.typeUnions

trait AnyTypeUnion {

  type or[Y] <: AnyTypeUnion
  type union // kind of return
}

sealed trait either[X] extends TypeUnion[not[X]]

trait TypeUnion[T] extends AnyTypeUnion {

  type or[S] = TypeUnion[T with not[S]]
  type union = not[T]
}

case object TypeUnion {
  type empty = either[Nothing]
}

private[cosas] sealed trait not[-T]
