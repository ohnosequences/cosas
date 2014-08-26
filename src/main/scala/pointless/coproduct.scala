package ohnosequences.pointless

import AnyTypeUnion._

import shapeless.{Nat, Succ}, Nat._

trait AnyCoproduct {

  type Types <: AnyTypeUnion
  type Bound

  type Length <: Nat
}

trait CNil extends AnyCoproduct {

  type Types = either[ (_0, Nothing) ]
  type Bound = Types#union

  type Length = _0
  type Head = Nothing
}

object CNil extends CNil

class :+:[H, T <: AnyCoproduct] extends AnyCoproduct {

  type Me = this.type

  type Types = T#Types#or[ (Length, Head) ]

  type Bound = Types#union

  type Length = Succ[T#Length]
  type Head = H

  def at[N <: Nat, V](pos: N, value: V)(implicit ev: (N,V) isOneOf Types): ValueOf[Me] = At[N,V,Me](pos, value)
}

object AnyCoproduct {

  implicit def coproductOps[C <: AnyCoproduct] = CoproductOps[C]()
}

case class CoproductOps[C <: AnyCoproduct]() {

  def at[N <: Nat, V](pos: N)(value: V)(implicit ev: (N,V) isOneOf C#Types): At[N,V,C] = At(pos, value)
}

sealed trait ValueOf[C <: AnyCoproduct] {

  type At <: Nat
  type Value
  val value: Value

  val ev: (At,Value) isOneOf C#Types
}

case class At[N <: Nat, V, C <: AnyCoproduct](val pos: N, val value: V)(implicit val ev: (N,V) isOneOf C#Types)
extends ValueOf[C] {

  type At = N
  type Value = V
}