package ohnosequences.cosas.types

import ohnosequences.cosas._, fns._, klists._

case object syntax {


  final case class TypeSyntax[T <: AnyType](val tpe: T) extends AnyVal {

    final def :=[@specialized V <: T#Raw](v: V): T := V = Denotation[T, V](tpe, v)
    final def apply[@specialized V <: T#Raw](v: V): T := V = Denotation[T, V](tpe, v)

    final def ==>[S <: AnyType](s: S): T ==> S = new (T ==> S)(tpe, s)
  }

  final case class DenotationSyntax[D <: AnyDenotation](val d: D) extends AnyVal {

    def =~=(w: D#Tpe := D#Value): Boolean = d.value == w.value
  }

  // final case class BuhArgh[T <: AnyProductType](val t: AnyProductType.Is[T]) {
  //
  //   def :×:[H <: T#Types#Bound](h: H): H :×: T = new :×:(h,t)
  // }

  final case class AnyProductTypeSyntax[H0 <: AnyType](val h: H0) extends AnyVal {

    def ×[
      T0 <: AnyProductType {
        type Types <: AnyKList { type Bound >: H0 <: AnyType }
        type Raw <: AnyKList { type Bound >: (H0 := H0#Raw) <: AnyDenotation { type Tpe <: Types#Bound }  }
      }
    ]
    (t: T0): H0 × T0 = new ×[H0,T0](t,h)
  }

  final case class AnyProductTypeDenotationSyntax[L <: AnyProductType, Vs <: L#Raw](val vs: L := Vs) extends AnyVal {

    // def project[T <: AnyType, V <: T#Raw](t: T)(implicit
    //   p: AnyApp1At[Project[L,T], L := Vs] { type Y = T := V }
    // )
    // : T := V =
    //   p( vs )

    def at[D <: AnyDenotation, N <: AnyNat](position: N)(implicit
      getAt: AnyApp1At[Vs at N, Vs] { type Y = D }
    )
    : D =
      getAt(vs.value)

    def getFirst[D <: AnyDenotation { type Tpe = T }, T <: AnyType](tpe: T)(implicit
      get: AnyApp1At[find[D], Vs] { type Y = D }
    )
    : D =
      get(vs.value)
  }
}
