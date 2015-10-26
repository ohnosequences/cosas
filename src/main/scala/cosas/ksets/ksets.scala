package ohnosequences.cosas.ksets

import ohnosequences.cosas._, klists._, typeUnions._

trait AnyKSet extends Any { kset =>

  type Elements <: AnyKList // { type Bound = kset.Bound }
  type Bound >: Elements#Bound <: Elements#Bound
  def elements: Elements

  def :~:[E <: Bound](e: E)(implicit ev: E isNotOneOf Elements#Types)
  : KSet[E :: Elements] =
    KSet[E :: Elements](e :: elements)
}

case class KEmpty[+B](val w: KEmpty.type) extends AnyVal with AnyKSet {

  type Elements = KNil[B] @uv
  type Bound = B @uv
  def elements: Elements = KNil[B]
}
case class KSet[+El <: AnyKList] private (val elements: El) extends AnyVal with AnyKSet {

  type Elements = El @uv
  type Bound = Elements#Bound @uv
}

case object KSet {


}
