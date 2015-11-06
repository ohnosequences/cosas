package ohnosequences.cosas.records

import ohnosequences.cosas._, types._, fns._, klists._

class Update[RT <: AnyRecordType] extends DepFn2[
  RT#Raw, KList.Of[AnyDenotation],
  AnyDenotationOf[RT]
]

case object Update {

  implicit def default[
    RT <: AnyRecordType,
    Ps <: KList.Of[AnyDenotation],
    Vs <: RT#Raw
  ]
  (implicit
    // check: Ps ⊂ RT#Raw,
    replace: AnyApp2At[replace[Vs], Vs, Ps] { type Y = Vs }
  )
  : AnyApp2At[Update[RT], Vs, Ps] { type Y = RT := Vs }=
    App2 {
      (recV: Vs, propReps: Ps) => new (RT := Vs)(replace(recV, propReps))
    }
}

// class GetField[RT <: AnyRecordType, T <: AnyType] extends DepFn1[
//   AnyDenotation { type Tpe = RT },
//   AnyDenotation { type Tpe = T }
// ]
