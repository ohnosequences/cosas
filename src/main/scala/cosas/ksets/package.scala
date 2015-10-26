package ohnosequences.cosas

import ohnosequences.cosas.klists._

package object ksets {

  type ∅[+A] = KEmpty[A]
  def ∅[A]: ∅[A] = KEmpty[A](KEmpty)
  type :~:[+E <: L#Bound, +L <: AnyKSet] = KSet[E :: L#Elements]


}
