package ohnosequences.cosas.types

import ohnosequences.cosas._, fns._, klists._

class Project[Ts <: AnyProductType, T <: AnyType] extends DepFn1[
  AnyDenotation { type Tpe = Ts },
  AnyDenotation { type Tpe = T }
]

case object Project extends ProjectInTail {

  implicit def foundInHead[
    H0 <: AnyType { type Raw >: V }, V,
    Ts <: AnyProductType {
      type Types <: AnyKList { type Bound >: H0 <: AnyType }
      type Raw <: AnyKList { type Bound >: (H0 := H0#Raw) <: AnyDenotation { type Tpe <: Types#Bound; type Value <: Types#Bound#Raw } }
    },
    Ds <: Ts#Raw
  ]
  : AnyApp1At[
      Project[H0 × Ts, H0],
      (H0 × Ts) := ((H0 := V) :: Ds)
    ] { type Y = H0 := V } =
    App1 { x: (H0 × Ts) := ((H0 := V) :: Ds) => x.value.head  }
}

trait ProjectInTail {

  implicit def foundInTail[
    H0 <: AnyType { type Raw >: V }, V,
    Ts <: AnyProductType {
      type Types <: AnyKList { type Bound >: H0 <: AnyType }
      type Raw <: AnyKList { type Bound >: (H0 := H0#Raw) <: AnyDenotation { type Tpe <: Types#Bound; type Value <: Types#Bound#Raw } }
    },
    Ds <: Ts#Raw,
    P <: AnyType { type Raw >: W }, W
  ]
  (implicit
    proj: AnyApp1At[Project[Ts,P], Ts := Ds] { type Y = P := W }
  )
  : AnyApp1At[
    Project[H0 × Ts, P],
    (H0 × Ts) := ((H0 := V) :: Ds)
  ] { type Y = P := W } =
    App1 { x: (H0 × Ts) := ((H0 := V) :: Ds) => proj( x.tpe.tail := x.value.tail ) }
}
