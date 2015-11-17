package ohnosequences

package object cosas {

  type ?≃[X, Y] = Either[X,Y]
  type <≃>[A, B] = (A ?≃ B) => (A ≃ B)


  type !=[A,B]  = Distinct[A,B]
  type !<[A,B]  = A NotSubtypeOf B
  type ≤[A,B]   = A SubtypeOf B

  type  _0 = zero.type
  val   _0: _0 = zero

  type  _1 = Successor[_0]
  val   _1: _1 = Successor(_0)

  type  _2 = Successor[_1]
  val   _2: _2 = Successor(_1)

  type  _3 = Successor[_2]
  val   _3: _3 = Successor(_2)

  type  _4 = Successor[_3]
  val   _4 = Successor(_3)
  // ...

  type True = TRUE.type
  val  True: True = TRUE
  type False = FALSE.type
  val  False: False = FALSE

  def ofType[X]: Witness[X] = Witness(Witness)
  def witness[A]: Witness[A] = new Witness[A](Witness)
  def /[A]: Witness[A] = new Witness[A](Witness)
  type /[A] = Witness[A]

  private[cosas] type uv = scala.annotation.unchecked.uncheckedVariance
}
