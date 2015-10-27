package ohnosequences

package object cosas {

  type ?≃[X, Y] = Either[X,Y]
  type <≃>[A, B] = (A ?≃ B) => (A ≃ B)

  def ofType[X]: Witness[X] = Witness(Witness)

  type !=[A,B]  = Distinct[A,B]
  type !<[A,B]  = A NotSubtypeOf B
  type ≤[A,B]   = A SubtypeOf B

  type _0 = zero.type
  type _1 = Successor[_0]
  type _2 = Successor[_1]
  type _3 = Successor[_2]
  type _4 = Successor[_3]
  // ...

  def witness[A]: Witness[A] = new Witness[A](Witness)

  private[cosas] type uv = scala.annotation.unchecked.uncheckedVariance
}
