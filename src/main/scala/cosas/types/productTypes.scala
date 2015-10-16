package ohnosequences.cosas.types

import ohnosequences.cosas._, klists._, fns._

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


class Project[Ts <: AnyProductType, T <: AnyType] extends DepFn1[
  AnyDenotation,
  AnyDenotation
]

case object Project extends ProjectInTail {

  implicit def foundInHead[
    H <: AnyType { type Raw >: V }, V,
    Ts <: AnyProductType { type Raw >: Ds }, Ds <: AnyKList { type Bound = AnyDenotation}
  ]
  : App1[
      Project[H :×: Ts, H],
      (H :×: Ts) := ((H := V) :: Ds),
      H := V
    ] =
    App1 { x: (H :×: Ts) := ((H := V) :: Ds) => x.value.head  }
}

trait ProjectInTail {

  implicit def foundInTail[
    H <: AnyType { type Raw >: V }, V,
    Ts <: AnyProductType { type Raw >: Ds }, Ds <: AnyKList { type Bound = AnyDenotation },
    P <: AnyType { type Raw >: W }, W
  ]
  (implicit
    proj: App1[Project[Ts,P], Ts := Ds, P := W ]
  )
  : App1[
    Project[H :×: Ts, P],
    (H :×: Ts) := ((H := V) :: Ds),
    P := W
  ] =
    App1 { x: (H :×: Ts) := ((H := V) :: Ds) => proj( new (Ts := Ds)(x.value.tail)) }
}
