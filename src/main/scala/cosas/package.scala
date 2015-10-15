package ohnosequences

package object cosas {

  type ?≃[X, Y] = Either[X,Y]
  type <≃>[A, B] = (A ?≃ B) => (A ≃ B)

  def ofType[X]: TypeRef[X] = TypeRef(TypeRef)

  type !=[A,B]  = Distinct[A,B]
  type !<[A,B]  = A NotSubtypeOf B
  type ≤[A,B]   = A SubtypeOf B
}
