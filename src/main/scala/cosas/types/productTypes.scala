package ohnosequences.cosas.types

import ohnosequences.cosas._, klists._, fns._

// TODO reproduce KList
trait AnyProductType extends AnyType { prod =>

  type Bound = Types#Bound

  type  Types <: AnyKList { type Bound <: AnyType } //{ type Bound = prod.Bound }
  val   types: Types

  type Raw <: AnyKList { type Bound = AnyDenotation }
}

case object AnyProductType {

  implicit def productTypeSyntax[L <: AnyProductType](l: L)
  : syntax.AnyProductTypeSyntax[L] =
    syntax.AnyProductTypeSyntax(l)

  implicit def productTypeDenotationSyntax[L <: AnyProductType, Vs <: L#Raw](ds: L := Vs)
  : syntax.AnyProductTypeDenotationSyntax[L,Vs] =
    syntax.AnyProductTypeDenotationSyntax(ds.value)

}

class EmptyProductType[E <: AnyType] extends AnyProductType {

  // type Bound = E
  type Types = *[E]
  val types: Types = *[E]

  type Raw = *[AnyDenotation]

  val label: String = "()"
}

case class :×:[H <: T#Bound, T <: AnyProductType](val head: H, val tail: T) extends AnyProductType {

  // type Bound = T#Bound
  type Types = H :: T#Types
  val  types: Types = head :: (tail.types: T#Types)

  type Raw = (H := H#Raw) :: T#Raw

  lazy val label: String = s"${head.label} :×: ${tail.label}"
}
