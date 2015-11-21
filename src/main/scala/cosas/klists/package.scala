package ohnosequences.cosas

import ohnosequences.cosas.fns._

package object klists {

  type  ::[+H <: T#Bound, +T <: AnyKList] = KCons[H,T]

  type  *[+A] = KNilOf[A]
  def   *[A]  : *[A] = new KNilOf[A]

  // NOTE should be removed?
  type  KNil[+A] = KNilOf[A]
  def   KNil[A]: KNil[A] = new KNilOf[A]

  type cons = Cons.type
  val  cons: cons = Cons

  type snoc = Snoc.type
  val  snoc: snoc = Snoc

  type noDuplicates = NoDuplicates.type
  val  noDuplicates: noDuplicates = NoDuplicates
}
