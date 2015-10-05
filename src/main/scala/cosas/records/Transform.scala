package ohnosequences.cosas.records

import ohnosequences.cosas._, types._, fns._, records._, typeSets._

// NOTE the Other value could be eliminated
class Transform[RT <: AnyRecord, Other <: AnyRecord]
extends DepFn3[ValueOf[RT], Other, AnyTypeSet, ValueOf[Other]]

case object Transform {

  implicit def default[
    RT <: AnyRecord,
    Other <: AnyRecord,
    Rest <: AnyTypeSet,
    Uni <: AnyTypeSet,
    Missing <: AnyTypeSet
  ](implicit
    missing: App2[subtract, Other#Raw, RT#Raw, Missing],
    allMissing: Rest ~:~ Missing,
    uni: App2[union, RT#Raw, Rest, Uni], //(RT#Raw ∪ Rest) { type Out = Uni },
    project: App1[take[Other#Raw],Uni,Other#Raw]
  )
  : App3[Transform[RT,Other], ValueOf[RT], Other, Rest, ValueOf[Other]] =
    App3 { (recRaw: ValueOf[RT], other: Other, rest: Rest) => other := project(uni(recRaw.value, rest)) }

}
