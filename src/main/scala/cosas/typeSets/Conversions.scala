package ohnosequences.cosas.typeSets

import ohnosequences.cosas._, fns._, typeSets._, types._

class toListOf[X] extends DepFn1[AnyTypeSet, List[X]]

case object toListOf {

  implicit def empty[X]: App1[toListOf[X], ∅, List[X]] =
    App1 { s: ∅ => Nil }

  implicit def nonEmpty[H <: X, X, T <: AnyTypeSet](implicit
    ap: App1[toListOf[X],T,List[X]]
  ): App1[toListOf[X], H :~: T, List[X]] =
    App1 { xs: H :~: T => xs.head :: toListOf(xs.tail) }
}

class toTypeMap[K <: AnyType,V] extends DepFn1[AnyTypeSet, Map[K,V]]

case object toTypeMap {

  implicit def empty[K <: AnyType, V]:App1[toTypeMap[K,V], ∅, Map[K,V]] =
    App1 { s: ∅ => Map() }

  implicit def nonEmpty[
    K <: AnyType, V,
    D <: AnyDenotation { type Tpe <: K; type Value <: V },
    T <: AnyTypeSet
  ](implicit
    ev: App1[toTypeMap[K,V], T, Map[K,V]],
    key: D#Tpe
  )
  : App1[toTypeMap[K,V], D :~: T, Map[K,V]] =
    App1 { s: D :~: T => toTypeMap(s.tail) + (key -> s.head.value) }
}
