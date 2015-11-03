package ohnosequences.cosas.types

import ohnosequences.cosas._, fns._, klists._

case object syntax {


  final case class TypeSyntax[T <: AnyType](val tpe: T) extends AnyVal {

    final def :=[@specialized V <: T#Raw](v: V): T := V = new (V Denotes T)(v)
    final def apply[@specialized V <: T#Raw](v: V): T := V = new (V Denotes T)(v)

    final def ==>[S <: AnyType](s: S): T ==> S = new (T ==> S)(tpe,s)
  }

  final case class AnyProductTypeSyntax[L <: AnyProductType](val l: L) extends AnyVal {

    def :×:[H <: AnyType, T <: AnyProductType](h: H): H :×: L =
      new :×:(h,l)
  }

  final case class AnyProductTypeDenotationSyntax[L <: AnyProductType, Vs <: L#Raw](val vs: Vs) extends AnyVal {

    def project[T <: AnyType, V <: T#Raw](t: T)(implicit
      p: App1[Project[L,T], L := Vs, T := V]
    )
    : T := V =
      p( new (L := Vs)(vs) )

    def at[D <: AnyDenotation, N <: AnyNat](position: N)(implicit
      getAt: AnyApp1At[Vs at N, Vs] { type Y = D }
    )
    : D =
      getAt(vs)

    def getFirst[D <: AnyDenotation { type Tpe = T }, T <: AnyType](tpe: T)(implicit
      get: AnyApp1At[D findIn Vs, Vs] { type Y = D }
    )
    : D =
      get(vs)
  }

  final case class RecordTypeDenotationSyntax[RT <: AnyRecordType, Vs <: RT#Raw](val vs: Vs) extends AnyVal {

    def get[D <: AnyDenotation { type Tpe = T }, T <: AnyType](tpe: T)(implicit
      get: AnyApp1At[D findIn Vs, Vs] { type Y = D }
    )
    : D =
      get(vs)
  }

  case class SubsetTypeSyntax[W <: AnyType, ST <: SubsetType[W]](val st: ST) extends AnyVal {

    final def apply[R <: W#Raw](raw: W := R): Option[ST := R] = {

      if ( st predicate raw ) None else Some( new (ST := R)(raw.value) )
    }

    final def withValue[R <: W#Raw](raw: W := R): Option[ST := R] = apply(raw)
  }
}
