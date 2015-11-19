package ohnosequences.cosas.types

import ohnosequences.cosas._, fns._, klists._

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
  : AnyApp1At[
    Project[H :×: Ts, P],
    (H :×: Ts) := ((H := V) :: Ds)
  ] { type Y = P := W } =
    App1 { x: (H :×: Ts) := ((H := V) :: Ds) => proj( new (Ts := Ds)(x.value.tail)) }
}
