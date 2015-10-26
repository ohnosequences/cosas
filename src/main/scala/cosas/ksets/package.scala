package ohnosequences.cosas

import ohnosequences.cosas.klists._

package object ksets {

  type ∅[+A] = KEmpty[A]
  def ∅[A]: ∅[A] = KEmpty[A](KNil[A])
  type :~:[+E <: L#Bound, +L <: AnyKSet] = KSet[E :: L#Elements]


}
