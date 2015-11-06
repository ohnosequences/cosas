package ohnosequences.cosas.types

import ohnosequences.cosas._, klists._, fns._, typeUnions._

sealed trait AnyRecordType extends AnyType {

  type Types <: AnyProductType

  type Raw >: Types#Raw <: Types#Raw
}

class RecordType[Ts <: AnyProductType](val types: Ts)(implicit ev: NoDuplicates[Ts#Types]) extends AnyRecordType {

  type Types = Ts

  type Raw = Types#Raw

  lazy val label: String = toString
}

case object AnyRecordType {

  implicit def altRecordSyntax[Ts <: AnyProductType, Vs <: Ts#Raw](rv: RecordType[Ts] := Vs)
  : syntax.RecordTypeDenotationSyntax[RecordType[Ts],Vs] =
    syntax.RecordTypeDenotationSyntax(rv.value)
    
  implicit def recordDenotationSyntax[RT <: AnyRecordType,Vs <: RT#Types#Raw](rv: RT := Vs)
  : syntax.RecordTypeDenotationSyntax[RT,Vs] =
    syntax.RecordTypeDenotationSyntax(rv.value)
}

// TODO move to predicate
trait NoDuplicates[L <: AnyKList]

case object NoDuplicates {

  implicit def empty[A]: NoDuplicates[KNil[A]] = new NoDuplicates[KNil[A]] {}

  implicit def nonEmpty[H <: T#Bound, T <: AnyKList](implicit
    ev: NoDuplicates[T],
    noH: H isNotOneOf T#Types
  )
  : NoDuplicates[H :: T] = new NoDuplicates[H :: T] {}
}

class UpdateRecord[RT <: AnyRecordType] extends DepFn2[
  RT#Raw, KList.Of[AnyDenotation],
  AnyDenotationOf[RT]
]

case object UpdateRecord {

  implicit def default[
    RT <: AnyRecordType,
    Ps <: KList.Of[AnyDenotation],
    Vs <: RT#Raw
  ]
  (implicit
    // check: Ps ⊂ RT#Raw,
    replace: App2[replace[Vs], Vs, Ps, Vs]
  )
  : App2[UpdateRecord[RT], Vs, Ps, RT := Vs] =
    App2 {
      (recV: Vs, propReps: Ps) => new (RT := Vs)(replace(recV, propReps))
    }
}

class GetField[RT <: AnyRecordType, T <: AnyType] extends DepFn1[
  AnyDenotation { type Tpe = RT },
  AnyDenotation { type Tpe = T }
]
