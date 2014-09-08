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

  type At = _0
  type Value = Nothing

  // val ev = implicitly[ (At,Value) isOneOf Types ]
}

object CNil extends CNil

trait :+:[H, T <: AnyCoproduct] extends AnyCoproduct {

  type Me = this.type

  type Types = T#Types#or[ (Length, Head) ]

  type Bound = Types#union

  type Length = Succ[T#Length]
  type Head = H

  def at[N <: Nat, V](pos: N, value: V)(implicit ev: (N,V) isOneOf Types): ValueOfCoproduct[Me] = At[N,V,Me](pos, value)
}

sealed trait ValueOfCoproduct[C <: AnyCoproduct] extends AnyCoproduct {

  type Types = C#Types
  type Bound = C#Bound
  type Length = C#Length

  type At <: Nat
  type Value
  val value: Value
}

case class At[N <: Nat, V, C <: AnyCoproduct](val pos: N, val value: V)(implicit val ev: (N,V) isOneOf C#Types)
extends ValueOfCoproduct[C] {

  type At = N
  type Value = V
}
