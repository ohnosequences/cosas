package ohnosequences.cosas

import ohnosequences.cosas.fns._

package object klists {

  type  ::[+H <: T#Bound, +T <: AnyKList] = KCons[H,T]

  type  *[+A] = KNilOf[A]
  def   *[A]  : *[A] = new KNilOf[A]

  // NOTE should be removed?
  type  KNil[+A] = KNilOf[A]
  def   KNil[A]: KNil[A] = new KNilOf[A]

  type  toListOf[L <: AnyKList.Of[B], B] = ToListOf[L,B]
  def   toListOf[L <: AnyKList.Of[B], B] : toListOf[L,B] = new ToListOf[L,B]

  type  toList[L <: AnyKList] = ToList[L]
  def   toList[L <: AnyKList] : toList[L] = new ToList[L]

  type  findIn[A] = FindIn[A]
  def   findIn[A] : findIn[A] = new FindIn[A]

  type  pick[E] = Pick[E]
  def   pick[E] : pick[E] = new Pick[E]

  type  replace[L <: AnyKList] = Replace[L]
  def   replace[L <: AnyKList]: replace[L] = new Replace[L]

  type  takeFirst[Q <: AnyKList] = TakeFirst[Q]
  def   takeFirst[Q <: AnyKList]: takeFirst[Q] = new TakeFirst[Q]

  type cons = Cons.type
  val  cons: cons = Cons

  type snoc = Snoc.type
  val  snoc: snoc = Snoc

  type noDuplicates = NoDuplicates.type
  val  noDuplicates: noDuplicates = NoDuplicates
}
