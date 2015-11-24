package ohnosequences.cosas

package object typeUnions {

  private[cosas] type just[+T] = not[not[T]]
  type empty = either[Nothing]

  type or[T <: AnyTypeUnion, S] = T#or[S]

  @annotation.implicitNotFound(msg = "Can't prove that ${X} is one of ${U}")
  type    isOneOf[X, U <: AnyTypeUnion] = just[X] ≤  U#union

  @annotation.implicitNotFound(msg = "Can't prove that ${X} is not one of ${U}")
  type isNotOneOf[X, U <: AnyTypeUnion] = just[X] !< U#union

  final type oneOf[U <: AnyTypeUnion] = {
    type    is[X] = X    isOneOf U
    type isNot[X] = X isNotOneOf U
  }

  @annotation.implicitNotFound(msg = "Can't prove that ${V} is subunion of ${U}")
  type    isSubunionOf[V <: AnyTypeUnion, U <: AnyTypeUnion] = V#union ≤  U#union

  @annotation.implicitNotFound(msg = "Can't prove that ${V <: AnyTypeUnion} is not subunion of ${U}")
  type isNotSubunionOf[V <: AnyTypeUnion, U <: AnyTypeUnion] = V#union !< U#union
}
