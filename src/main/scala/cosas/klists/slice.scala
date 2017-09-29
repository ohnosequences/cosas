package ohnosequences.cosas.klists

import ohnosequences.cosas._, fns._, typeUnions._

class slice[N <: AnyNat, M <: AnyNat]
extends Composition[drop[N], take[M]]

case object slice {

  implicit def zeroRight[L <: AnyKList, N <: AnyNat]
  : AnyApp1At[slice[N,_0], L] { type Y = KNil[L#Bound] } =
    App1 { l: L =>
      // println{"optimizing empty segment: right = 0"}
      KNil[L#Bound]
    }
  // NOTE for when M ≤ N
  implicit def emptySegment[L <: AnyKList, N <: AnyNat, M <: AnyNonZeroNat](implicit
    ev: M#Pred isOneOf N#StrictlySmaller
  )
  : AnyApp1At[slice[N,M], L] { type Y = KNil[L#Bound] } =
    App1 { l: L =>
      // println{"optimizing empty segment: left ≥ right"}
      KNil[L#Bound]
    }
}
