package ohnosequences.cosas.types

import ohnosequences.cosas.klists._

trait AnyProductType extends AnyType {

  type Types <: AnyKList { type Bound = AnyType }
  val types: Types

  type Raw <: AnyKList { type Bound = AnyDenotation }
}

case object AnyProductType {

  implicit def productTypeSyntax[L <: AnyProductType](l: L): AnyProductTypeSyntax[L] =
    AnyProductTypeSyntax(l)
}

case object EmptyProductType extends AnyProductType {

  type Types = KNil[AnyType]
  val  types = KNil[AnyType]

  type Raw = KNil[AnyDenotation]

  val label: String = "□"
}

case class :×:[H <: AnyType, T <: AnyProductType](val head: H, val tail: T) extends AnyProductType {

  type Types = H :: T#Types
  val  types: Types = head :: tail.types

  type Raw = AnyDenotationOf[H] :: T#Raw

  lazy val label: String = s"${head.label} :×: ${tail.label}"
}

case class AnyProductTypeSyntax[L <: AnyProductType](val l: L) extends AnyVal {

  def :×:[H <: AnyType, T <: AnyProductType](h: H): H :×: L =
    new :×:(h,l)
}
