package ohnosequences.cosas.types

import ohnosequences.cosas._, fns._, klists._

case object syntax {


  final case class TypeSyntax[T <: AnyType](val tpe: T) extends AnyVal {

    final def :=[@specialized V <: T#Raw](v: V): T := V = new (V Denotes T)(tpe, v)
    final def apply[@specialized V <: T#Raw](v: V): T := V = new (V Denotes T)(tpe, v)

    final def ==>[S <: AnyType](s: S): T ==> S = new (T ==> S)(tpe, s)
  }

  final case class DenotationSyntax[T <: AnyType, V <: T#Raw](val v: V) extends AnyVal {

    def =~=(w: T := V): Boolean = v == w.value
  }

  final case class AnyProductTypeSyntax[L <: AnyProductType](val l: L) extends AnyVal {

    def :×:[H0 <: L#Types#Bound](h: H0): H0 :×: L =
      new :×:[H0,L](h,l)
  }

  final case class AnyProductTypeDenotationSyntax[L <: AnyProductType, Vs <: L#Raw](val vs: L := Vs) extends AnyVal {

    def project[T <: AnyType, V <: T#Raw](t: T)(implicit
      p: AnyApp1At[Project[L,T], L := Vs] { type Y = T := V }
    )
    : T := V =
      p( vs )

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
