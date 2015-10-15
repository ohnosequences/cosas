package ohnosequences.cosas.typeSets

import ohnosequences.cosas._, fns._, typeSets._, types._

class ToListOf[X] extends DepFn1[AnyTypeSet, List[X]]

case object ToListOf {

  implicit def empty[X]: App1[ToListOf[X], ∅, List[X]] =
    App1 { s: ∅ => Nil }

  implicit def nonEmpty[H <: X, X, T <: AnyTypeSet](implicit
    ap: App1[ToListOf[X],T,List[X]]
  ): App1[ToListOf[X], H :~: T, List[X]] =
    App1 { xs: H :~: T => xs.head :: ap(xs.tail) }
}

class ToTypeMap[K <: AnyType,V] extends DepFn1[AnyTypeSet, Map[K,V]]

case object ToTypeMap {

  implicit def empty[K <: AnyType, V]:App1[toTypeMap[K,V], ∅, Map[K,V]] =
    App1 { s: ∅ => Map() }

  implicit def nonEmpty[
    K <: AnyType, V,
    D <: AnyDenotation { type Tpe <: K; type Value <: V },
    T <: AnyTypeSet
  ](implicit
    toTypeMap: App1[toTypeMap[K,V], T, Map[K,V]],
    key: D#Tpe
  )
  : App1[toTypeMap[K,V], D :~: T, Map[K,V]] =
    App1 { s: D :~: T => toTypeMap(s.tail) + (key -> s.head.value) }
}
