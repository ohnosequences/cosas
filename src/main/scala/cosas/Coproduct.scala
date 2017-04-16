package ohnosequences.cosas

import AnyTypeUnion._

import shapeless.{Nat, Succ}, Nat._

object AnyCoproduct {

  type size[C <: AnyCoproduct] = C#Types#Arity

  final class valueOf[C <: AnyCoproduct] {

    // we could do something like Inl/Inr here
    def apply[N <: Nat, V](pos: N, value: V)(implicit 
      ev: (N,V) isOneOf C#Types
    )
    : ValueOfCoproduct[C] = At[N,V,C](pos, value)

  }

  object Coproduct {

    def apply[C <: AnyCoproduct] = new valueOf[C]
  }
  
}

trait AnyCoproduct {

  type Types <: AnyTypeUnion
  type Bound = Types#union
}

trait CNil extends AnyCoproduct {

  type Types = empty
}

object CNil extends CNil

trait :+:[H, T <: AnyCoproduct] extends AnyCoproduct {

  type Types = (Succ[AnyCoproduct.size[T]], H) :∨: T#Types
}

sealed trait ValueOfCoproduct[C <: AnyCoproduct] extends AnyCoproduct {

  type Types = C#Types

  type At <: Nat
  type Value
  val value: Value
}

final case class here[H,T <: AnyCoproduct](val value: H) extends ValueOfCoproduct[:+:[H, T]] {

  type At = AnyCoproduct.size[H :+: T]
  type Value = H
}
final case class right[H, T <: AnyCoproduct, VC <: ValueOfCoproduct[T]](val other: VC) extends ValueOfCoproduct[:+:[H, T]] {

  type At = VC#At
  type Value = VC#Value
  lazy val value = other.value
}

case class At[N <: Nat, V, C <: AnyCoproduct](val pos: N, val value: V)(implicit val ev: (N,V) isOneOf C#Types)
extends ValueOfCoproduct[C] {

  type At = N
  type Value = V
}