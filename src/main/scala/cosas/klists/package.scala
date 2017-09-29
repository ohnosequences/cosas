package ohnosequences.cosas

package object klists {

  type  ::[+H <: T#Bound, +T <: AnyKList] = KCons[H,T]

  type  *[+A] = KNilOf[A]
  def   *[A]  : *[A] = new KNilOf[A]

  // NOTE should be removed?
  type  KNil[+A] = KNilOf[A]
  def   KNil[A]: KNil[A] = new KNilOf[A]

  type cons = Cons.type
  val  cons: Cons.type = Cons

  type snoc = Snoc.type
  val  snoc: Snoc.type = Snoc

  type uncons = Uncons.type
  val uncons: Uncons.type = Uncons

  type noDuplicates = NoDuplicates.type
  val  noDuplicates: NoDuplicates.type = NoDuplicates
}
