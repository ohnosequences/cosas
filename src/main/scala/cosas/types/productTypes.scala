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

// TODO parametric on Type
class EmptyProductType[E <: AnyType] extends AnyProductType {

  // type Bound = E
  type Types = *[E]
  val types: Types = *[E]

  type Raw = *[AnyDenotation]

  val label: String = "()"
}
case object EmptyProductType extends AnyProductType {

  // type Bound = AnyType
  type Types = *[AnyType]
  val  types = *[AnyType]

  type Raw = *[AnyDenotation]

  val label: String = "()"
}

case class :×:[H <: T#Bound, T <: AnyProductType](val head: H, val tail: T) extends AnyProductType {

  // type Bound = T#Bound
  type Types = H :: T#Types
  val  types: Types = head :: (tail.types: T#Types)

  type Raw = AnyDenotation { type Tpe = H } :: T#Raw

  lazy val label: String = s"${head.label} :×: ${tail.label}"
}

class Project[Ts <: AnyProductType, T <: AnyType] extends DepFn1[
  AnyDenotation { type Tpe = Ts },
  AnyDenotation { type Tpe = T }
]

case object Project extends ProjectInTail {

  implicit def foundInHead[
    H <: Ts#Types#Bound { type Raw >: V }, V,
    Ts <: AnyProductType { type Raw >: Ds }, Ds <: AnyKList { type Bound = AnyDenotation }
  ]
  : AnyApp1At[
      Project[H :×: Ts, H],
      (H :×: Ts) := ((H := V) :: Ds)
    ] { type Y = H := V } =
    App1 { x: (H :×: Ts) := ((H := V) :: Ds) => x.value.head  }
}

trait ProjectInTail {

  implicit def foundInTail[
    H <: Ts#Types#Bound { type Raw >: V }, V,
    Ts <: AnyProductType { type Raw >: Ds }, Ds <: AnyKList { type Bound = AnyDenotation },
    P <: AnyType { type Raw >: W }, W
  ]
  (implicit
    proj: AnyApp1At[Project[Ts,P], Ts := Ds] { type Y = P := W }
  )
  : App1[
    Project[H :×: Ts, P],
    (H :×: Ts) := ((H := V) :: Ds),
    P := W
  ] =
    App1 { x: (H :×: Ts) := ((H := V) :: Ds) => proj( new (Ts := Ds)(x.value.tail)) }
}
