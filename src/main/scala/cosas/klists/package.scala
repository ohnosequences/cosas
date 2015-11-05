package ohnosequences.cosas

import ohnosequences.cosas.fns._

package object klists {

  type  *[+A] = KNilOf[A]
  def   *[A]  : *[A] = new KNilOf[A]

  type  KNil[+A] = KNilOf[A]
  def   KNil[A]: KNil[A] = new KNilOf[A]

  type  ::[+H <: T#Bound, +T <: AnyKList] = KCons[H,T]

  type  mapKList[F <: AnyDepFn1] = MapKListOf[F, F#In1, F#Out]
  def   mapKList[F <: AnyDepFn1] : mapKList[F] = new mapKList[F]

  type  concatenate[L <: AnyKList] = Concatenate[L]
  def   concatenate[L <: AnyKList] : Concatenate[L] = new Concatenate[L]

  type  toListOf[L <: KList.Of[B], B] = ToListOf[L,B]
  def   toListOf[L <: KList.Of[B], B] : toListOf[L,B] = new ToListOf[L,B]

  type  toList[L <: AnyKList] = ToList[L]
  def   toList[L <: AnyKList] : toList[L] = new ToList[L]

  type  findIn[A <: L#Bound, L <: AnyKList] = (A FindIn L)
  def   findIn[A <: L#Bound, L <: AnyKList] : (A findIn L) = new (A FindIn L)

  type  pick[E] = Pick[E]
  def   pick[E] : pick[E] = new Pick[E]

  type  filter[A] = Filter[A]
  def   filter[A]: filter[A] = new Filter[A]
}
