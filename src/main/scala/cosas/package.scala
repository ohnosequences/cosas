package ohnosequences

package object cosas {

  type ?≃[X, Y] = Either[X,Y]
  type <≃>[A, B] = (A ?≃ B) => (A ≃ B)

  def ofType[X]: TypeRef[X] = TypeRef(TypeRef)
}
