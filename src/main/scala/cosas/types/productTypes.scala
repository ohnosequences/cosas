package ohnosequences.cosas.types

import ohnosequences.cosas._, klists._

// TODO reproduce KList
trait AnyProductType extends AnyType {

  type Types <: AnyKList { type Bound <: AnyType } //{ type Bound = prod.Bound } thanks scalac
  val  types: Types

  type Raw <: AnyKList.withBound[AnyDenotation]
}

case object AnyProductType {
  type Of[+B <: AnyType] = AnyProductType { type Types <: AnyKList.Of[B] }
  type withBound[B <: AnyType] = AnyProductType { type Types <: AnyKList.withBound[B] }

  implicit def productTypeSyntax[L <: AnyProductType](l: L)
  : syntax.AnyProductTypeSyntax[L] =
    syntax.AnyProductTypeSyntax(l)

  implicit def productTypeDenotationSyntax[L <: AnyProductType, Vs <: L#Raw](ds: L := Vs)
  : syntax.AnyProductTypeDenotationSyntax[L,Vs] =
    syntax.AnyProductTypeDenotationSyntax(ds)

}

class EmptyProductType[E <: AnyType] extends AnyProductType {

  type Types = *[E]
  val types: Types = *[E]

  type Raw = *[AnyDenotation]

  val label: String = "()"
}

case class :×:[H <: T#Types#Bound, T <: AnyProductType](val head: H, val tail: T) extends AnyProductType {

  type            Types = H :: T#Types
  lazy val types: Types = head :: (tail.types: T#Types)

  type Raw = AnyDenotation { type Tpe = H } :: T#Raw

  lazy val label: String = s"${head.label} :×: ${tail.label}"
}
