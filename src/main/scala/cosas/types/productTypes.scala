package ohnosequences.cosas.types

import ohnosequences.cosas._, klists._, fns._

trait AnyProductType extends AnyType {

  type  Types <: AnyKList { type Bound = AnyType }
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

case object EmptyProductType extends AnyProductType {

  type Types = *[AnyType]
  val  types = *[AnyType]

  type Raw = *[AnyDenotation]

  val label: String = "()"
}

case class :×:[H <: AnyType, T <: AnyProductType](val head: H, val tail: T) extends AnyProductType {

  type Types = H :: T#Types
  val  types: Types = head :: tail.types

  type Raw = AnyDenotationOf[H] :: T#Raw

  lazy val label: String = s"${head.label} :×: ${tail.label}"
}

class Project[Ts <: AnyProductType, T <: AnyType] extends DepFn1[
  AnyDenotation { type Tpe = Ts },
  AnyDenotation { type Tpe = T }
]

case object Project extends ProjectInTail {

  implicit def foundInHead[
    H <: AnyType { type Raw >: V }, V,
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
    H <: AnyType { type Raw >: V }, V,
    Ts <: AnyProductType { type Raw >: Ds }, Ds <: AnyKList { type Bound = AnyDenotation },
    P <: AnyType { type Raw >: W }, W
  ]
  (implicit
    proj: AnyApp1At[Project[Ts,P], Ts := Ds] { type Y = P := W }
  )
  : AnyApp1At[
    Project[H :×: Ts, P],
    (H :×: Ts) := ((H := V) :: Ds)]{
    type Y = P := W
  } =
    App1 { x: (H :×: Ts) := ((H := V) :: Ds) => proj( new (Ts := Ds)(x.value.tail)) }

  // implicit def usingFindIn[
  //   Ds <: AnyDenotation { type Tpe = Ts; type Value = Vs }, Vs <: AnyKList { type Bound = AnyDenotation }, Ts <: AnyProductType,
  //   D <: AnyDenotation { type Tpe = T }, T <: AnyType
  // ](implicit
  //   find: AnyApp1At[D findIn Vs, Vs] { type Y = D }
  // )
  // : App1[Project[Ts,T],Ds,D] =
  //   App1 { ds: Ds => find(ds.value) }
}
